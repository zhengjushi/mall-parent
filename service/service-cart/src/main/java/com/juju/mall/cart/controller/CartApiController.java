package com.juju.mall.cart.controller;

import com.juju.mall.cart.CartInfo;
import com.juju.mall.cart.service.CartService;
import com.juju.mall.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @RequestMapping("addCart")
    public void addCart(@RequestBody CartInfo cartInfo){
        cartService.addCart(cartInfo);
    }

    /**
     * 获取购物车列表
     * @param request
     * @return
     */
    @RequestMapping("cartList")
    public Result cartList(HttpServletRequest request){
        String userId = request.getHeader("userId");

        if(StringUtils.isBlank(userId)){
            userId = request.getHeader("userTempId");
        }
        List<CartInfo> cartInfos = new ArrayList<>();
        cartInfos = cartService.cartList(userId);
        return Result.ok(cartInfos);
    }

    /**
     * 根据用户id获取被选中的购物车商品列表
     * @param userId
     * @return
     */
    @RequestMapping("getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId")String userId){
        List<CartInfo> list = cartService.getCartCheckedList(userId);
        return list;
    }
}
