package com.juju.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juju.mall.entity.BaseTrademark;
import com.juju.mall.result.Result;
import com.juju.mall.service.BaseTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@CrossOrigin
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @GetMapping("baseTrademark/getTrademarkList")
    public Result<List<BaseTrademark>> getTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list(null);
        return Result.ok(list);
    }

    @GetMapping("baseTrademark/{page}/{size}")
    public Result<IPage<BaseTrademark>> getTrademarkListByPage(@PathVariable("page")Long page,@PathVariable("size")Long size){
        IPage<BaseTrademark> iPage = new Page<>();
        iPage.setCurrent(page);
        iPage.setSize(size);
        IPage<BaseTrademark> page1 = baseTrademarkService.page(iPage, null);
        return Result.ok(page1);
    }

    @PostMapping("baseTrademark/save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }
}
