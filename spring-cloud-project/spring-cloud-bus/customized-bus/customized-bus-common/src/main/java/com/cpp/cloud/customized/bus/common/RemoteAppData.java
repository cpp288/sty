package com.cpp.cloud.customized.bus.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程事件调用数据
 *
 * @author chenjian
 * @date 2018-12-10 17:18
 */
@Getter
@Setter
public class RemoteAppData {

    /**
     * 发送方应用名称
     */
    private String sender;

    /**
     * 发送数据
     */
    private Object value;

    /**
     * 发送事件类型
     */
    private String eventType;
}
