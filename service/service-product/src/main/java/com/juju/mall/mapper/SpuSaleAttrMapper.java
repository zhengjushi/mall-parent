package com.juju.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juju.mall.entity.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {
    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("spu_id") Long spuId,@Param("sku_id") Long skuId);

}
