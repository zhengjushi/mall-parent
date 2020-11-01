package com.juju.mall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.entity.BaseCategory2;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseCategory2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 二级分类表 前端控制器
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/admin/product/")
@CrossOrigin
public class BaseCategory2Controller {


    @Autowired
    private BaseCategory2Service category2Service;

    @GetMapping("getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getCategory2ByCategory1Id(@PathVariable("category1Id")String category1Id){
        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<>();
        wrapper.eq("category1_id",category1Id);
        List<BaseCategory2> list = category2Service.list(wrapper);
        return Result.ok(list);
    }

}

