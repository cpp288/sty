package com.cpp.cloud.stream.sink;

import com.cpp.cloud.stream.sink.samples.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-11-28 13:50
 */
@SpringBootApplication
@EnableBinding({Sink.class, MySink.class})
public class SinkSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinkSamplesApplication.class, args);
    }
}
