package com.cpp.cloud.hystrix.controller;

import com.cpp.cloud.hystrix.annotation.CircuitBreaker;
import com.cpp.cloud.hystrix.interceptor.CustomizedControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 自定义实现熔断
 *
 * @author chenjian
 * @date 2018-12-02 21:36
 */
@RestController
public class CustomizedController {

    private final static Random RANDOM = new Random();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 简易容错版本
     *
     * @param message
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/simple/say")
    public String simpleSay(@RequestParam String message) throws Exception {
        Future<String> future = executorService.submit(() -> doSay(message));

        String returnValue = null;
        try {
            // 线程执行时间超过100ms执行容错方法
            returnValue = future.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            returnValue = errorContent();
        }
        return returnValue;
    }

    /**
     * 中级熔断版本
     * 使用 ControllerAdvice 去拦截异常，或者使用 HandlerInterceptor 自定义实现
     *
     * @param message
     * @return
     * @throws Exception
     * @see CustomizedControllerAdvice
     * @see HandlerInterceptor
     */
    @GetMapping(value = "/middle/say")
    public String middleSay(@RequestParam String message) throws Exception {
        Future<String> future = executorService.submit(() -> doSay(message));

        String returnValue = null;
        try {
            // 线程执行时间超过100ms执行容错方法
            returnValue = future.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // 如果超时，则在线程运行时将其中断
            future.cancel(true);
            throw e;
        }
        return returnValue;
    }

    /**
     * 高级熔断版本（注解超时、并发控制）
     * 通过注解来设置超时时间、并发数量，用aop的方式进行切面限制
     *
     * @param message
     * @return
     * @throws Exception
     * @see CircuitBreaker
     * @see com.cpp.cloud.hystrix.aop.CustomizedControllerAspect
     */
    @GetMapping(value = "/advanced/say")
    @CircuitBreaker(timeout = 100, concurrent = 1)
    public String advancedSay(@RequestParam String message) throws Exception {
        return doSay(message);
    }

    /**
     * 具体执行方法体
     *
     * @param message
     * @return
     * @throws InterruptedException
     */
    private String doSay(String message) throws InterruptedException {
        // 方法随机睡眠时间
        int value = RANDOM.nextInt(200);
        System.out.println("say costs " + value + "ms.");

        Thread.sleep(value);

        String returnValue = "say : " + message;
        System.out.println(returnValue);

        return returnValue;
    }

    /**
     * 错误返回信息
     *
     * @return
     */
    private String errorContent() {
        return "Error";
    }
}
