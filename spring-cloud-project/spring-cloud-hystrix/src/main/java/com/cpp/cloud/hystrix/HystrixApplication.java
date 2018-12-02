package com.cpp.cloud.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-02 21:34
 */
@SpringBootApplication
@EnableHystrix
@EnableAspectJAutoProxy(proxyTargetClass = true)    // 使用 AspectJ CGLIB
public class HystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class, args);
    }
}
