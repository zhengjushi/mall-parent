package com.juju.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juju.mall.user.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressMapper extends BaseMapper<UserAddress> {
}
