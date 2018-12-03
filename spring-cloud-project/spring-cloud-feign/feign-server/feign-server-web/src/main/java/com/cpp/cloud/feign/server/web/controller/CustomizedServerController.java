package com.cpp.cloud.feign.server.web.controller;

import com.cpp.cloud.feign.server.api.CustomizedServerApi;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义 Feign 服务端api实现
 *
 * @author chenjian
 * @date 2018-12-03 23:35
 */
@RestController
public class CustomizedServerController implements CustomizedServerApi {

    @Override
    public String say(@RequestParam String message) {
        System.out.printf("接收到消息：message -> %s\n", message);
        return "Hello, " + message;
    }
}
