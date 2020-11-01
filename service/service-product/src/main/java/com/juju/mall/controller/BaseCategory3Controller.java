package com.juju.mall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.entity.BaseCategory3;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 三级分类表 前端控制器
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/admin/product/")
@CrossOrigin
public class BaseCategory3Controller {

    @Autowired
    private BaseCategory3Service category3Service;

    @GetMapping("getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getCategory3ByCategory2Id(@PathVariable("category2Id")String category2Id){
        QueryWrapper<BaseCategory3> wrapper = new QueryWrapper<>();
        wrapper.eq("category2_id",category2Id);
        List<BaseCategory3> list = category3Service.list(wrapper);
        return Result.ok(list);
    }

}

