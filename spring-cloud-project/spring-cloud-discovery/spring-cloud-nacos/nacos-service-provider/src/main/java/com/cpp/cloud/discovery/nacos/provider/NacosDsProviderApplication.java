package com.cpp.cloud.discovery.nacos.provider;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-12 17:38
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosDsProviderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NacosDsProviderApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
