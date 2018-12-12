package com.cpp.cloud.discovery.nacos.consumer;

import com.cpp.cloud.discovery.nacos.consumer.api.ProviderApi;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-12 17:38
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = ProviderApi.class)
public class NacosDsConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NacosDsConsumerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
