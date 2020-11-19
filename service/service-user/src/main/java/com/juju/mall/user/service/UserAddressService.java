package com.juju.mall.user.service;

import com.juju.mall.user.UserAddress;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> findUserAddressListByUserId(String userId);
}
