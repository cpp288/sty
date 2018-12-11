package com.cpp.cloud.stream.samples.source;

import com.cpp.cloud.stream.samples.source.stream.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-11-28 13:50
 */
@SpringBootApplication
@EnableBinding(MessageService.class)
public class SourceSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceSamplesApplication.class, args);
    }

    @StreamListener(MessageService.TEST_INPUT)
    public void receive(Message<String> message) {
        System.out.printf("receive message %s\n", message.getPayload());
    }
}
