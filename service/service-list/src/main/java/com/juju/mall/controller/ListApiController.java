package com.juju.mall.controller;

import com.juju.mall.list.SearchParam;
import com.juju.mall.list.SearchResponseVo;
import com.juju.mall.result.Result;
import com.juju.mall.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/list")
public class ListApiController {

    @Autowired
    private ListService listService;

    @RequestMapping("onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")String skuId){
        listService.onSale(skuId);
        return Result.ok();
    }

    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")String skuId){
        listService.cancelSale(skuId);
        return Result.ok();
    }

    @GetMapping("hostScore/{skuId}")
    public void hotScore(@PathVariable("skuId") String skuId){
        listService.hotScore(skuId);
    }

    @RequestMapping("list")
    SearchResponseVo list(@RequestBody SearchParam searchParam){
        SearchResponseVo list = listService.list(searchParam);
        return list;
    }
}