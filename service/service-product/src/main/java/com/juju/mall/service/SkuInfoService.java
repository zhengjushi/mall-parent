package com.juju.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SkuInfo;

public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);
}
