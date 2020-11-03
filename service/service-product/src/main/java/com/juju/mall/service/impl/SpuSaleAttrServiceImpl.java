package com.juju.mall.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.entity.SpuSaleAttrValue;
import com.juju.mall.mapper.SpuSaleAttrMapper;
import com.juju.mall.service.SpuSaleAttrService;
import com.juju.mall.service.SpuSaleAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr> implements SpuSaleAttrService {

    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;

    @Override
    public List<SpuSaleAttr> spuSaleAttrList(String spuId) {
        QueryWrapper<SpuSaleAttr> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuSaleAttr> spuSaleAttrs = baseMapper.selectList(wrapper);
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            QueryWrapper<SpuSaleAttrValue> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("spu_id",spuId);
            wrapper1.eq("base_sale_attr_id",spuSaleAttr.getBaseSaleAttrId());
            List<SpuSaleAttrValue> list = spuSaleAttrValueService.list(wrapper1);
            spuSaleAttr.setSpuSaleAttrValueList(list);
        }
        return spuSaleAttrs;
    }
}
