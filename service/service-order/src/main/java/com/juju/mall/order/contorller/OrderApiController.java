package com.juju.mall.order.contorller;

import com.juju.mall.client.cart.CartFeignClient;
import com.juju.mall.order.service.OrderService;
import com.juju.mall.result.Result;
import com.juju.mall.user.UserAddress;
import com.juju.mall.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/order")
public class OrderApiController {



    @Autowired
    private OrderService orderService;

    @GetMapping("auth/trade/{userId}")
    public Map<String,Object> trade(@PathVariable("userId") String userId){

        Map<String,Object> map = orderService.trade(userId);
        return map;
    }
}
