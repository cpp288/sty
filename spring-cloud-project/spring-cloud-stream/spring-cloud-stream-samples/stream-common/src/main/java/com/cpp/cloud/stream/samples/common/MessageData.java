package com.cpp.cloud.stream.samples.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 消息数据
 *
 * @author chenjian
 * @date 2018-12-11 14:33
 */
@Getter
@Setter
@ToString
public class MessageData {

    private int id;
    private String message;
}
