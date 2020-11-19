package com.juju.mall.client.cart;

import com.juju.mall.cart.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-cart")
public interface CartFeignClient {

    @RequestMapping("api/cart/addCart")
    void addCart(@RequestBody CartInfo cartInfo);

    @RequestMapping("api/cart/getCartCheckedList/{userId}")
    List<CartInfo> getCartCheckedList(@PathVariable("userId")String userId);
}
