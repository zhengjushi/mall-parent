package com.juju.mall.controller;


import com.juju.mall.entity.BaseCategory1;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseCategory1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 一级分类表 前端控制器
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/admin/product/")
public class BaseCategory1Controller {

    @Autowired
    private BaseCategory1Service category1Service;

    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getCategory1(){
        List<BaseCategory1> list = category1Service.list(null);

        return Result.ok(list);
    }
}

