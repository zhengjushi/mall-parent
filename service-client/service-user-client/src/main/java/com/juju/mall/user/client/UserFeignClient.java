package com.juju.mall.user.client;

import com.juju.mall.user.UserAddress;
import com.juju.mall.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-user")
public interface UserFeignClient {

    @RequestMapping("api/user/passport/verify/{token}")
    UserInfo verify(@PathVariable("token")String token);

    @RequestMapping("api/user/passport/findUserAddressListByUserId/{userId}")
    List<UserAddress> findUserAddressListByUserId(@PathVariable("userId") String userId);
}
