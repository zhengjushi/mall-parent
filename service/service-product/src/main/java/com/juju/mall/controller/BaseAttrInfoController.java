package com.juju.mall.controller;


import com.juju.mall.entity.BaseAttrInfo;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 属性表 前端控制器
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/admin/product")
public class BaseAttrInfoController {

    @Autowired
    private BaseAttrInfoService attrInfoService;

    @GetMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result<List<BaseAttrInfo>> attrInfoList(@PathVariable("category1Id")String category1Id,
                                                   @PathVariable("category2Id")String category2Id,
                                                   @PathVariable("category3Id")String category3Id){
        List<BaseAttrInfo> list =  attrInfoService.attrInfoList(category1Id,category2Id,category3Id);
        return Result.ok(list);
    }

}

