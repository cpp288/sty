package com.cpp.cloud.stream.sink.samples;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * 接收服务
 *
 * @author chenjian
 * @date 2018-11-28 15:12
 */
@EnableBinding(Sink.class)
public class ReceiveService {

    @StreamListener(Sink.INPUT)
    public void receive(String msg) {
        System.out.println(msg);
    }
}
