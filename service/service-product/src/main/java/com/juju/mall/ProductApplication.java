package com.juju.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.juju.mall.mapper")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
