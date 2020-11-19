package com.juju.mall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juju.mall.cart.CartInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper extends BaseMapper<CartInfo> {
}
