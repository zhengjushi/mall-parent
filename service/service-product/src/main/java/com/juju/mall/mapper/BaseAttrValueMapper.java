package com.juju.mall.mapper;

import com.juju.mall.entity.BaseAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 属性值表 Mapper 接口
 * </p>
 *
 * @author juju
 * @since 2020-10-31
 */
public interface BaseAttrValueMapper extends BaseMapper<BaseAttrValue> {

    List<BaseAttrValue> getAttrValueList(int id);

}
