package com.juju.mall.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.*;
import com.juju.mall.mapper.SkuInfoMapper;
import com.juju.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;


    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        baseMapper.insert(skuInfo);
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuInfo.getId());
            skuAttrValueService.save(skuAttrValue);
        }
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImageList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);

    }
}