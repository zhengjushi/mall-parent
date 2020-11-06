package com.juju.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);


    SkuInfo getSkuById(String skuId);

    BigDecimal getSkuPrice(String skuId);

    List<SkuImage> getSkuImages(String skuId);
}
