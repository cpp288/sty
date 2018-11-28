package com.cpp.cloud.stream.source.samples;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * 消息发送
 *
 * @author chenjian
 * @date 2018-11-28 14:42
 */
@EnableBinding(Source.class)
public class SendService {

    @Resource
    private Source source;

    public void sendMsg(String message) {
        source.output().send(MessageBuilder.withPayload(message).build());
    }
}
