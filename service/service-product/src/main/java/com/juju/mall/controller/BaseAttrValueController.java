package com.juju.mall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.entity.BaseAttrValue;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 属性值表 前端控制器
 * </p>
 *
 * @author juju
 * @since 2020-10-31
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/product")
public class BaseAttrValueController {

    @Autowired
    private BaseAttrValueService baseAttrValueService;

    @GetMapping("/getAttrValueList/{attrId}")
    public Result<List<BaseAttrValue>> getAttrValueList(@PathVariable("attrId") String attrId){
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",attrId);
        List<BaseAttrValue> list = baseAttrValueService.list(wrapper);
        return Result.ok(list);
    }
}

