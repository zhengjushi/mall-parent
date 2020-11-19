package com.juju.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.list.client.ListFeignClient;
import com.juju.mall.product.client.ProductFeignClient;
import com.juju.mall.service.ItemService;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ListFeignClient listFeignClient;

    @Override
    public Map<String, Object> getItem(String skuId) {
        long start = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();

        CompletableFuture<SkuInfo> completableFutureSku = CompletableFuture.supplyAsync(new Supplier<SkuInfo>() {
            @Override
            public SkuInfo get() {
                SkuInfo skuInfo = productFeignClient.getSkuById(skuId);
                List<SkuImage> skuImageList = productFeignClient.getSkuImages(skuId);
                skuInfo.setSkuImageList(skuImageList);
                map.put("skuInfo", skuInfo);
                return skuInfo;
            }
        },threadPoolExecutor);

        CompletableFuture<Void> completableFutureCategoryView = completableFutureSku.thenAcceptAsync(new Consumer<SkuInfo>() {
            @Override
            public void accept(SkuInfo skuInfo) {
                BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
                map.put("categoryView", categoryView);
            }
        },threadPoolExecutor);

        CompletableFuture<Void> completableFuturevalueMaps = completableFutureSku.thenAcceptAsync(new Consumer<SkuInfo>() {
            @Override
            public void accept(SkuInfo skuInfo) {
                List<Map<String, Object>> valuesMap = productFeignClient.getSaleAttrValueBySpuId(skuInfo.getSpuId());
                Map<String, Object> valueMap = new HashMap<>();
                for (Map<String, Object> vmap : valuesMap) {
                    valueMap.put(vmap.get("valueIds") + "", vmap.get("sku_id"));
                }
                map.put("valuesSkuJson", JSON.toJSONString(valueMap));
            }
        },threadPoolExecutor);

        CompletableFuture<Void> completableFutureSaleAttr = completableFutureSku.thenAcceptAsync(new Consumer<SkuInfo>() {
            @Override
            public void accept(SkuInfo skuInfo) {
                List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrs(skuInfo.getSpuId(), Long.valueOf(skuId));
                map.put("spuSaleAttrList", spuSaleAttrList);
            }
        },threadPoolExecutor);

        CompletableFuture<Void> completableFuturePrice = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                BigDecimal price = productFeignClient.getSkuPrice(skuId);
                map.put("price", price);
            }
        },threadPoolExecutor);

        CompletableFuture.allOf(completableFutureSku,completableFutureCategoryView,completableFuturePrice,completableFutureSaleAttr,completableFuturevalueMaps).join();

        long end = System.currentTimeMillis();
        System.out.println("总耗时：--> " +(end-start));

        //记录该商品在搜索中的热度值
        listFeignClient.hotScore(skuId);


        return map;
    }
}
