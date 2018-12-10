package com.cpp.cloud.customized.bus.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-10 17:35
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class CustomizedBusServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CustomizedBusServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
