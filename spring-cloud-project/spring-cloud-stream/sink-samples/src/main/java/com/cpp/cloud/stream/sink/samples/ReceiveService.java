package com.cpp.cloud.stream.sink.samples;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * 接收服务
 *
 * @author chenjian
 * @date 2018-11-28 15:12
 */
@Service
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
    public void receiveMyInput(Message<String> msg) {
        // 获取消息体
        String message = msg.getPayload();
        // 获取消息头
        MessageHeaders headers = msg.getHeaders();
        System.out.printf("[mysink]接收到消息：%s\n", message);
    }
}
