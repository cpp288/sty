package com.cpp.cloud.stream.source;

import com.cpp.cloud.stream.source.samples.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-11-28 13:50
 */
@SpringBootApplication
@EnableBinding({Source.class, MySource.class})
public class SourceSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceSamplesApplication.class, args);
    }
}
