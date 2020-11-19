package com.juju.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartController {

    @RequestMapping("cart/cart.html")
    public String cartList(){
        return "cart/index";
    }
}
