package com.cpp.cloud.feign.client;

import com.cpp.cloud.feign.customized.annotation.EnableRestClient;
import com.cpp.cloud.feign.server.api.CustomizedServerApi;
import com.cpp.cloud.feign.server.api.ServerApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-03 10:02
 */
@SpringBootApplication
@EnableFeignClients(clients = ServerApi.class)
@EnableDiscoveryClient
@EnableRestClient(clients = CustomizedServerApi.class)  // 自定义 @EnableFeignClients
public class FeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
