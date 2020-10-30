package com.juju.mall.controller;

import com.juju.mall.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping("test")
    public Result<String> test(){
        return Result.ok("Hello World!!");
    }
}
