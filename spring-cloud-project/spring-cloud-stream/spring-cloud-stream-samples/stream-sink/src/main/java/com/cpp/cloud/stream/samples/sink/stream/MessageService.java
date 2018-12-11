package com.cpp.cloud.stream.samples.sink.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义
 *
 * @author chenjian
 * @date 2018-11-28 15:29
 * @see org.springframework.cloud.stream.messaging.Sink
 * @see org.springframework.cloud.stream.messaging.Source
 */
public interface MessageService {

    String TEST_INPUT = "test-input";

    String TEST_OUTPUT = "test-output";

    @Input(MessageService.TEST_INPUT)
    SubscribableChannel testInput();

    @Output(MessageService.TEST_OUTPUT)
    MessageChannel testOutput();
}
