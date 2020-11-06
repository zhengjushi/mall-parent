package com.juju.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SpuInfo;
import com.juju.mall.entity.SpuSaleAttr;

import java.util.List;

public interface SpuInfoService extends IService<SpuInfo> {
    IPage<SpuInfo> spuList(IPage<SpuInfo> iPage, String category3Id);

    void saveSpuInfo(SpuInfo spuInfo);

    List<SpuSaleAttr> getSpuSaleAttrs(Long spuId);
}
