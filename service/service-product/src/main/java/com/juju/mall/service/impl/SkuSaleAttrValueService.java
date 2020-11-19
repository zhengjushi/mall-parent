package com.juju.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.SkuSaleAttrValue;

import java.util.List;
import java.util.Map;

public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    List<Map<String, Object>> selectSaleAttrValueBySpuId(Long spuId);

}
