package com.juju.mall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.juju.mall")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.juju.mall")
public class ServiceCartApplication {
public static void main(String[] args) {

        SpringApplication.run(ServiceCartApplication.class,args);
    }
}
