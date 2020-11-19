package com.juju.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juju.mall.entity.SkuSaleAttrValue;

import java.util.List;
import java.util.Map;

public interface SkuSaleAttrValueMapper extends BaseMapper<SkuSaleAttrValue> {
    List<Map<String, Object>> selectSaleAttrValueBySpuId(Long spuId);
}
