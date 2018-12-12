package com.cpp.cloud.discovery.nacos.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供者
 *
 * @author chenjian
 * @date 2018-12-12 18:01
 */
@RestController
public class ProviderController {

    @GetMapping("/echo/{message}")
    public String echo(@PathVariable String message) {
        return "Hello Nacos Discovery " + message;
    }
}
