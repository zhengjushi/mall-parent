package com.juju.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.juju.mall"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages= {"com.juju.mall"})
public class ServerGatewayApplication {
   public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class,args);
    }
}
