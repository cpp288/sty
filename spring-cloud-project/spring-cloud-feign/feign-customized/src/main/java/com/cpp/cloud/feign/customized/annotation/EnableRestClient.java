package com.cpp.cloud.feign.customized.annotation;

import com.cpp.cloud.feign.customized.RestClientsRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义，用于启动 RestClient，相当于 Feign 中的 @EnableFeignClients
 *
 * @author chenjian
 * @date 2018-12-03 19:57
 * @see EnableFeignClients
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RestClientsRegistrar.class)
public @interface EnableRestClient {

    /**
     * 指定 @RestClient 接口
     *
     * @return
     */
    Class<?>[] clients() default {};
}
