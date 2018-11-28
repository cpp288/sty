package com.cpp.cloud.stream.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-11-28 13:50
 */
@SpringBootApplication
public class RabbitMqSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqSourceApplication.class, args);
    }
}
