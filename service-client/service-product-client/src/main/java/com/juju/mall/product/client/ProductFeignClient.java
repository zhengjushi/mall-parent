package com.juju.mall.product.client;

import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("api/product/getSkuById/{skuId}")
    SkuInfo getSkuById(@PathVariable("skuId") String skuId);

    @GetMapping("api/product/getSkuPrice/{skuId}")
    BigDecimal getSkuPrice(@PathVariable("skuId") String skuId);

    @GetMapping("api/product/getSpuSaleAttrs/{spuId}")
    List<SpuSaleAttr> getSpuSaleAttrs(@PathVariable("spuId") Long spuId);

    @GetMapping("api/product/getSkuImages/{skuId}")
    List<SkuImage> getSkuImages(@PathVariable("skuId") String skuId);

    @GetMapping("api/product/getCategoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id);
}
