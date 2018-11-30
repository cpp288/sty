package com.cpp.cloud.ribbon.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * server controller
 *
 * @author chenjian
 * @date 2018-11-30 14:20
 */
@RestController
public class ServerController {

    /**
     * 服务端接口
     *
     * @param message
     * @return
     */
    @GetMapping("/say")
    public String say(@RequestParam String message) {
        System.out.printf("接收到消息：message -> %s\n", message);
        return "Hello, " + message;
    }
}
