package com.cpp.cloud.stream.source.samples;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 默认output send service
 *
 * @author chenjian
 * @date 2018-11-28 14:42
 */
@Service
public class SendService {

    @Resource
    private Source source;
    @Resource
    private MySource mySource;

    public void sendBySource(String message) {
        source.output().send(MessageBuilder.withPayload(message).build());
    }

    public void sendByMySource(String message) {
        mySource.myOutput().send(MessageBuilder.withPayload(message).build());
    }

}
