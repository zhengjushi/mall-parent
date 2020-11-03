package com.juju.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SpuInfo;

public interface SpuInfoService extends IService<SpuInfo> {
    IPage<SpuInfo> spuList(IPage<SpuInfo> iPage, String category3Id);

    void saveSpuInfo(SpuInfo spuInfo);
}
