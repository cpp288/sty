package com.cpp.cloud.stream.sink.samples;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义sink
 *
 * @author chenjian
 * @date 2018-11-28 15:29
 * @see org.springframework.cloud.stream.messaging.Sink
 */
public interface MySink {

    String INPUT = "myInput";

    @Input(MySink.INPUT)
    MessageChannel myInput();
}
