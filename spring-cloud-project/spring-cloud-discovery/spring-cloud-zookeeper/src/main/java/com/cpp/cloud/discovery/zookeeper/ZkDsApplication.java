package com.cpp.cloud.discovery.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-11-29 09:42
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ZkDsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZkDsApplication.class, args);
    }
}
