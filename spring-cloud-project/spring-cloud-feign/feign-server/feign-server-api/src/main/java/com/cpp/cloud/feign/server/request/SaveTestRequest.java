package com.cpp.cloud.feign.server.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * save 测试请求体
 *
 * @author chenjian
 * @date 2018-12-03 10:28
 */
@Getter
@Setter
@ToString
public class SaveTestRequest {

    private String name;

    private String value;
}
