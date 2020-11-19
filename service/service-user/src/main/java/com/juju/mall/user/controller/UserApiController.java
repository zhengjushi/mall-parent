package com.juju.mall.user.controller;

import com.juju.mall.result.Result;
import com.juju.mall.user.UserAddress;
import com.juju.mall.user.UserInfo;
import com.juju.mall.user.service.UserAddressService;
import com.juju.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user/passport")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 用户授权
     * @param token
     * @return
     */
    @RequestMapping("verify/{token}")
    public UserInfo verify(@PathVariable("token")String token){
        return userService.verify(token);
    }

    /**
     * 用户登录
     * @param userInfo
     * @return
     */
    @RequestMapping("login")
    public Result login(@RequestBody UserInfo userInfo){

        Map<String,Object> map = userService.login(userInfo);
        if(null!=map){
            return Result.ok(map);
        }else {
            return Result.fail();
        }
    }


    /**
     * 获取用户地址信息列表
     * @param userId
     * @return
     */
    @RequestMapping("/findUserAddressListByUserId/{userId}")
    public List<UserAddress> findUserAddressListByUserId(@PathVariable("userId")String userId){
        List<UserAddress> list = userAddressService.findUserAddressListByUserId(userId);
        return list;
    }


    @RequestMapping("/findUserByUserId/{userId}")
    public UserInfo findUserByUserId(@PathVariable("userId") String userId){
        UserInfo userInfo = userService.findUserByUserId(userId);
        return userInfo;
    }



}
