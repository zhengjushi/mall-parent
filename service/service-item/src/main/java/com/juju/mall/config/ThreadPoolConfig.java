package com.juju.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 500, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

        return threadPoolExecutor;
    }

}