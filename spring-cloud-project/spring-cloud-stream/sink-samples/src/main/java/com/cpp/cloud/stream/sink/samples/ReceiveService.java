package com.cpp.cloud.stream.sink.samples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * 接收服务
 * 在接收的时候可以使用3中方式：
 *  1. 基于接口编程的方式 init()
 *  2. 使用 Spring cloud stream 注解驱动（最终也是基于 Spring Integration 实现的）
 *  3. 使用 Spring Integration 注解驱动
 * 以上这3种方式不能同时接收到消息
 *
 * @author chenjian
 * @date 2018-11-28 15:12
 */
@Service
public class ReceiveService {

    @Autowired
    private MySink mySink;

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
     * 基于接口编程
     */
    @PostConstruct
    public void init() {
        SubscribableChannel subscribableChannel = mySink.myInput();
        subscribableChannel.subscribe(message -> {
            MessageHeaders headers = message.getHeaders();
            String encoding = (String) headers.get("charset-encoding");
            byte[] content = (byte[]) message.getPayload();
            try {
                System.out.println("接收到消息：" + new String(content, encoding));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 使用 Spring cloud stream 注解驱动
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

    /**
     * 使用 Spring Integration 注解驱动
     *
     * @param message
     */
    @ServiceActivator(inputChannel = MySink.INPUT)
    public void onServiceActivator(String message) {
        System.out.printf("[onServiceActivator]接收到消息：%s\n", message);
    }
}
