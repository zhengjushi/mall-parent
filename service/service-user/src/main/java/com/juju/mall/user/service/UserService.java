package com.juju.mall.user.service;

import com.juju.mall.user.UserInfo;

import java.util.Map;

public interface UserService {
    Map<String, Object> login(UserInfo userInfo);

    UserInfo verify(String token);

    UserInfo findUserByUserId(String userId);
}
