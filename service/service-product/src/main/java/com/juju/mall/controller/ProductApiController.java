package com.juju.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.service.BaseCategoryViewService;
import com.juju.mall.service.SkuInfoService;
import com.juju.mall.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/product")
@CrossOrigin
public class ProductApiController {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private BaseCategoryViewService categoryViewService;


    @GetMapping("getSkuById/{skuId}")
    public SkuInfo getSkuById(@PathVariable("skuId")String skuId){
        SkuInfo byId = skuInfoService.getSkuById(skuId);
        return byId;
    }

    @GetMapping("getSkuPrice/{skuId}")
    BigDecimal getSkuPrice(@PathVariable("skuId") String skuId){
        BigDecimal price = skuInfoService.getSkuPrice(skuId);
        return price;
    }

    @GetMapping("getSpuSaleAttrs/{spuId}")
    List<SpuSaleAttr> getSpuSaleAttrs(@PathVariable("spuId") Long spuId){
        List<SpuSaleAttr> spuSaleAttrs = spuInfoService.getSpuSaleAttrs(spuId);
        return spuSaleAttrs;
    }

    @GetMapping("getSkuImages/{skuId}")
    List<SkuImage> getSkuImages(@PathVariable("skuId") String skuId){
        List<SkuImage> skuImageList = skuInfoService.getSkuImages(skuId);
        return skuImageList;
    }

    @GetMapping("getCategoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable("category3Id") Long category3Id){
        BaseCategoryView baseCategoryView = categoryViewService.getCategoryView(category3Id);
        return baseCategoryView;
    }


}
