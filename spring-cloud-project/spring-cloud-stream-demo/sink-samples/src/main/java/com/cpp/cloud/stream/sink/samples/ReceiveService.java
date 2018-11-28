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
@EnableBinding({Sink.class, MySink.class})
public class ReceiveService {

    /**
     * 默认 sink
     *
     * @param msg
     */
    @StreamListener(Sink.INPUT)
    public void receive(String msg) {
        System.out.printf("[sink]接收到消息：%s\n", msg);
    }

    /**
     * 自定义 sink
     *
     * @param msg
     */
    @StreamListener(MySink.INPUT)
    public void receiveMyInput(String msg) {
        System.out.printf("[mysink]接收到消息：%s\n", msg);
    }
}
