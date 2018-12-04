package com.cpp.cloud.feign.server.web;

import com.cpp.cloud.feign.server.web.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-03 10:02
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FeignServerApplication implements WebMvcConfigurer {

    @Autowired
    private SecurityInterceptor securityInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(FeignServerApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor).addPathPatterns("/feign/say");
    }
}
