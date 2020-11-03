package com.juju.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.result.Result;
import com.juju.mall.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/product")
@CrossOrigin
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        System.out.println(skuInfo);
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    @GetMapping("list/{page}/{limit}")
    public Result<IPage<SkuInfo>> skuInfoList(@PathVariable("page")Long page,@PathVariable("limit")Long limit){
        IPage<SkuInfo> infoIPage = new Page<>();
        infoIPage.setCurrent(page);
        infoIPage.setSize(limit);
        IPage<SkuInfo> page1 = skuInfoService.page(infoIPage, null);
        return Result.ok(page1);
    }

    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")String skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        skuInfo.setIsSale(1);
        skuInfoService.updateById(skuInfo);
        return Result.ok();
    }

    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")String skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        skuInfo.setIsSale(0);
        skuInfoService.updateById(skuInfo);
        return Result.ok();
    }
}
