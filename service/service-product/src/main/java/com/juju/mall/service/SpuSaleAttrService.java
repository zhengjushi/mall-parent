package com.juju.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SpuSaleAttr;

import java.util.List;

public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> spuSaleAttrList(String spuId);

}
