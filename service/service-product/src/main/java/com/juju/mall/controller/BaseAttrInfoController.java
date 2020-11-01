package com.juju.mall.controller;


import com.juju.mall.entity.BaseAttrInfo;
import com.juju.mall.entity.BaseAttrValue;
import com.juju.mall.response.AttrInfoVO;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
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


    @PostMapping("saveAttrInfo")
    public Result<String> saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        System.out.println(baseAttrInfo);
        attrInfoService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

}

