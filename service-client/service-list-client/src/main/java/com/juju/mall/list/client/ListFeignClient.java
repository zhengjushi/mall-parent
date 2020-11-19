package com.juju.mall.list.client;

import com.juju.mall.list.SearchParam;
import com.juju.mall.list.SearchResponseVo;
import com.juju.mall.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-list")
public interface ListFeignClient {

    @RequestMapping("api/list/onSale/{skuId}")
    Result onSale(@PathVariable("skuId")String skuId);

    @GetMapping("api/list/cancelSale/{skuId}")
    Result cancelSale(@PathVariable("skuId")String skuId);

    @GetMapping("api/list/hostScore/{skuId}")
    void hotScore(@PathVariable("skuId") String skuId);

    @RequestMapping("api/list/list")
    SearchResponseVo list(@RequestBody SearchParam searchParam);
}
