package com.juju.mall.product.client;

import com.alibaba.fastjson.JSONObject;
import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.list.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("api/product/getSkuById/{skuId}")
    SkuInfo getSkuById(@PathVariable("skuId") String skuId);

    @GetMapping("api/product/getSkuPrice/{skuId}")
    BigDecimal getSkuPrice(@PathVariable("skuId") String skuId);

    @GetMapping("api/product/getSpuSaleAttrs/{spuId}/{skuId}")
    List<SpuSaleAttr> getSpuSaleAttrs(@PathVariable("spuId") Long spuId,@PathVariable("skuId")Long skuId);

    @GetMapping("api/product/getSkuImages/{skuId}")
    List<SkuImage> getSkuImages(@PathVariable("skuId") String skuId);

    @GetMapping("api/product/getCategoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id);

    @GetMapping("api/product/getSaleAttrValueBySpuId/{spuId}")
    List<Map<String, Object>> getSaleAttrValueBySpuId(@PathVariable("spuId")Long spuId);

    @GetMapping("api/product/getGoodsBySkuId/{skuId}")
    Goods getGoodsBySkuId(@PathVariable("skuId") String skuId);

    @RequestMapping("api/product/categoryList")
    List<JSONObject> categoryList();

}
