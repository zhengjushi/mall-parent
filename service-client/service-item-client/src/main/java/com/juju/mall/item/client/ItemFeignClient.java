package com.juju.mall.item.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "service-item")
public interface ItemFeignClient {

    @RequestMapping("api/item/getItem/{skuId}")
    Map<String,Object> getItem(@PathVariable("skuId") String skuId);

}
