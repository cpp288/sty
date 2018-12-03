package com.cpp.cloud.feign.client.controller;

import com.cpp.cloud.feign.server.api.CustomizedServerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义 Feign Controller
 *
 * @author chenjian
 * @date 2018-12-03 22:33
 */
@RestController
public class CustomizedFeignController {

    @Autowired
    private CustomizedServerApi customizedServerApi;

    @GetMapping(value = "/rest/say")
    public String say(@RequestParam String message) {
        return customizedServerApi.say(message);
    }
}
