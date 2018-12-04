package com.cpp.cloud.stream.source.samples;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义output
 *
 * @author chenjian
 * @date 2018-11-28 15:21
 * @see org.springframework.cloud.stream.messaging.Source
 */
public interface MySource {

    String OUTPUT = "myOutput";

    @Output(MySource.OUTPUT)
    MessageChannel myOutput();
}
