package com.cpp.sharding.jdbc.simples;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-06 17:04
 */
@SpringBootApplication
@MapperScan("com.cpp.sharding.jdbc.simples.mapper")
public class ShardingJdbcSimplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcSimplesApplication.class, args);
    }
}
