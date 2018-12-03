package com.cpp.cloud.feign.client;

import com.cpp.cloud.feign.server.api.ServerApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-03 10:02
 */
@SpringBootApplication
@EnableFeignClients(clients = ServerApi.class)
@EnableDiscoveryClient
public class FeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }
}
