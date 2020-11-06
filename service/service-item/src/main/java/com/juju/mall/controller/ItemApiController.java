package com.juju.mall.controller;

import com.juju.mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/item")
@CrossOrigin
public class ItemApiController {


    @Autowired
    ItemService itemService;


    @GetMapping("getItem/{skuId}")
    Map<String,Object> getItem(@PathVariable("skuId") String skuId){
        Map<String,Object> map = itemService.getItem(skuId);
        System.out.println("这是map："+map);
        return map;
    }
}
