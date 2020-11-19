package com.juju.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PassportController {

    @RequestMapping("login.html")
    public String index(String originUrl, Model model){

        //请求被网关拦截后跳转到登录页面需要携带原始的请求地址
        //用户在passport登录后。要按照原始的请求地址跳转到原来的页面
        System.out.println(originUrl);
        model.addAttribute("originUrl",originUrl);
        return "login";
    }


}
