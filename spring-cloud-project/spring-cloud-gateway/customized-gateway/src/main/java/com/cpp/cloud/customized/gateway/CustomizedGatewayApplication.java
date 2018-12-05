package com.cpp.cloud.customized.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-05 23:21
 */
@SpringBootApplication
@EnableDiscoveryClient
@ServletComponentScan(basePackages = "com.cpp.cloud.customized.gateway.servlet")
public class CustomizedGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomizedGatewayApplication.class, args);
    }
}
