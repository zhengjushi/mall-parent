package com.juju.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.list.Goods;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);


    SkuInfo getSkuById(String skuId);

    BigDecimal getSkuPrice(String skuId);

    List<SkuImage> getSkuImages(String skuId);

    List<Map<String, Object>> getSaleAttrValueBySpuId(Long spuId);

    Goods getGoodsBySkuId(String skuId);
}
