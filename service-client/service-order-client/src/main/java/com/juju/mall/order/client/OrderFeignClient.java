package com.juju.mall.order.client;

import com.juju.mall.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FeignClient("service-order")
public interface OrderFeignClient {

    @GetMapping("api/order/auth/trade/{userId}")
    Map<String,Object> trade(@PathVariable("userId") String userId);
}
