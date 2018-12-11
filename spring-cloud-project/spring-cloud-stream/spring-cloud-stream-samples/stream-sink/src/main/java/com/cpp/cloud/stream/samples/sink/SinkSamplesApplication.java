package com.cpp.cloud.stream.samples.sink;

import com.cpp.cloud.stream.samples.common.MessageData;
import com.cpp.cloud.stream.samples.sink.stream.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-11-28 13:50
 */
@SpringBootApplication
@EnableBinding(MessageService.class)
public class SinkSamplesApplication {

    @Autowired
    private MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(SinkSamplesApplication.class, args);
    }

    /**
     * 基于接口编程
     */
    @PostConstruct
    public void init() {
        SubscribableChannel subscribableChannel = messageService.testInput();
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
    @StreamListener(MessageService.TEST_INPUT)
    public void receiveMyInput(Message<MessageData> msg) {
        // 获取消息体
        MessageData message = msg.getPayload();
        // 获取消息头
        MessageHeaders headers = msg.getHeaders();
        System.out.printf("[test]接收到消息：%s\n", message);
    }

    @StreamListener(MessageService.TEST_INPUT)
    public void receiveMyInput2(Message<MessageData> msg) {
        // 获取消息体
        MessageData message = msg.getPayload();
        // 获取消息头
        MessageHeaders headers = msg.getHeaders();
        System.out.printf("[test2]接收到消息：%s\n", message);
    }

    /**
     * 反馈消息
     *
     * @param msg
     * @return
     */
//    @StreamListener(MessageService.TEST_INPUT)
//    @SendTo(MessageService.TEST_OUTPUT)
//    public String sendToReceive(Message<MessageData> msg) {
//        System.out.printf("[test]接收到消息：%s\n", msg.getPayload());
//        return msg.getPayload() + " received";
//    }

    /**
     * 使用 Spring Integration 注解驱动
     *
     * @param message
     */
    @ServiceActivator(inputChannel = MessageService.TEST_INPUT)
    public void onServiceActivator(MessageData message) {
        System.out.printf("[onServiceActivator]接收到消息：%s\n", message);
    }
}
