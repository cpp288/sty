package com.cpp.cloud.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * hystrix熔断
 *
 * @author chenjian
 * @date 2018-12-02 23:37
 */
@RestController
public class HystrixController {

    private final static Random RANDOM = new Random();

    @HystrixCommand(fallbackMethod = "errorContent",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
            })
    @GetMapping(value = "/hystrix/say")
    public String hystrixSay(@RequestParam String message) throws InterruptedException {
        // 方法随机睡眠时间
        int value = RANDOM.nextInt(200);
        System.out.println("hystrix say costs " + value + "ms.");

        Thread.sleep(value);

        String returnValue = "hystrix say : " + message;
        System.out.println(returnValue);

        return returnValue;
    }

    private String errorContent(String message) {
        return "Error : " + message;
    }
}
