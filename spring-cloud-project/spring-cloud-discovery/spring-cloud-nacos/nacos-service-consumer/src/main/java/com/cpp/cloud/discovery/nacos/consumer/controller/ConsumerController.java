package com.cpp.cloud.discovery.nacos.consumer.controller;

import com.cpp.cloud.discovery.nacos.consumer.api.ProviderApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费端
 *
 * @author chenjian
 * @date 2018-12-12 17:51
 */
@RestController
public class ConsumerController {

    @Autowired
    private ProviderApi providerApi;

    @GetMapping("/echo/{message}")
    public String echo(@PathVariable String message) {

        return "收到 provider 数据：" + providerApi.echo(message);
    }
}
