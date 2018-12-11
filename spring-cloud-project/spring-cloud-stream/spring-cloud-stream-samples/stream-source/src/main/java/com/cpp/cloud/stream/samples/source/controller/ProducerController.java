package com.cpp.cloud.stream.samples.source.controller;

import com.cpp.cloud.stream.samples.common.MessageData;
import com.cpp.cloud.stream.samples.source.stream.MessageService;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 生产者接口
 *
 * @author chenjian
 * @date 2018-11-28 14:45
 */
@RestController
public class ProducerController {

    @Resource
    private MessageService messageService;

    /**
     * 自定义
     *
     * @param messageData
     */
    @PostMapping("/send")
    public void send(@RequestBody MessageData messageData) {
        messageService.testOutput().send(MessageBuilder.withPayload(messageData)
                .setHeader("charset-encoding", "utf-8").build());
    }

    /**
     * rocket mq
     *
     * @param messageData
     */
    @PostMapping("/rocket/send")
    public void sendByRocketMq(@RequestBody MessageData messageData) {
        messageService.rocketOutput().send(MessageBuilder.withPayload(messageData).build());
    }
}
