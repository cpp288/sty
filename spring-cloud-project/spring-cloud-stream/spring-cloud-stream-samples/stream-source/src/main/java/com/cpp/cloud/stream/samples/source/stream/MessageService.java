package com.cpp.cloud.stream.samples.source.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义output
 *
 * @author chenjian
 * @date 2018-11-28 15:21
 * @see org.springframework.cloud.stream.messaging.Source
 * @see org.springframework.cloud.stream.messaging.Sink
 */
public interface MessageService {

    String TEST_OUTPUT = "test-output";

    String ROCKET_OUTPUT = "rocket-output";

    String TEST_INPUT = "test-input";

    @Output(MessageService.TEST_OUTPUT)
    MessageChannel testOutput();

    @Output(MessageService.ROCKET_OUTPUT)
    MessageChannel rocketOutput();

    @Input(MessageService.TEST_INPUT)
    SubscribableChannel testInput();
}
