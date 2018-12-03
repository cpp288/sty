package com.cpp.cloud.feign.customized.annotation;

import org.springframework.cloud.openfeign.FeignClient;

import java.lang.annotation.*;

/**
 * 自定义 相当于 Feign 中的 @FeignClient
 *
 * @author chenjian
 * @date 2018-12-03 19:55
 * @see FeignClient
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestClient {

    /**
     * Rest服务的应用名称
     *
     * @return
     */
    String name();
}
