package com.juju.mall.controller;

import com.juju.mall.cart.CartInfo;
import com.juju.mall.client.cart.CartFeignClient;
import com.juju.mall.order.client.OrderFeignClient;
import com.juju.mall.result.Result;
import com.juju.mall.util.AuthContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    OrderFeignClient orderFeignClient;



    @RequestMapping("trade.html")
    public String trade(HttpServletRequest request, Model model){

        String userId = AuthContextHolder.getUserId(request);
        Map<String, Object> trade = orderFeignClient.trade(userId);
        for (Map.Entry<String, Object> stringObjectEntry : trade.entrySet()) {
            model.addAttribute(stringObjectEntry.getKey(),stringObjectEntry.getValue());
        }

        return "order/trade";
    }

    @RequestMapping("addCart.html")
    public String addCart(HttpServletRequest request, CartInfo cartInfo){
        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(userId)){
            userId = request.getHeader("userTempId");
        }
        cartInfo.setUserId(userId);
        cartFeignClient.addCart(cartInfo);
        return "redirect:http://cart.gmall.com/cart/success.html";
    }
}
