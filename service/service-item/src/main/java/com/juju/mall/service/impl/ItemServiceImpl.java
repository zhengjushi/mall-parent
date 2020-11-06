package com.juju.mall.service.impl;

import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.product.client.ProductFeignClient;
import com.juju.mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> getItem(String skuId) {
        SkuInfo skuInfo = productFeignClient.getSkuById(skuId);
        if (skuInfo == null){
            return null;
        }
        BigDecimal price = productFeignClient.getSkuPrice(skuId);
        List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrs(skuInfo.getSpuId());
        List<SkuImage> skuImageList = productFeignClient.getSkuImages(skuId);
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        Map<String, Object> map = new HashMap<>();
        skuInfo.setSkuImageList(skuImageList);
        map.put("skuInfo", skuInfo);
        map.put("categoryView", categoryView);
        map.put("price", price);
        map.put("spuSaleAttrList", spuSaleAttrList);
        return map;
    }
}
