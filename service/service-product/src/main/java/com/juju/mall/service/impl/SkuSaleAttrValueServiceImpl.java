package com.juju.mall.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.SkuAttrValue;
import com.juju.mall.entity.SkuSaleAttrValue;
import com.juju.mall.mapper.SkuAttrValueMapper;
import com.juju.mall.mapper.SkuSaleAttrValueMapper;
import com.juju.mall.service.SkuAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue> implements SkuSaleAttrValueService {


    @Override
    public List<Map<String, Object>> selectSaleAttrValueBySpuId(Long spuId) {
        List<Map<String, Object>> maps = baseMapper.selectSaleAttrValueBySpuId(spuId);
        return maps;
    }
}
