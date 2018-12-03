package com.cpp.cloud.feign.server.response;

import lombok.Builder;
import lombok.Data;

/**
 * save 测试响应体
 *
 * @author chenjian
 * @date 2018-12-03 10:31
 */
@Data
@Builder
public class SaveTestResponse {

    @Builder.Default
    private String result = "ok";

    private String message;
}
