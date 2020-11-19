package com.juju.mall.mapper;

import com.juju.mall.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juju.mall.list.SearchAttr;

import java.util.List;

/**
 * <p>
 * 属性表 Mapper 接口
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    List<BaseAttrInfo> selectAttrS(String categoryId);

    List<SearchAttr> selectBaseAttrInfoListBySkuId(String skuId);

}
