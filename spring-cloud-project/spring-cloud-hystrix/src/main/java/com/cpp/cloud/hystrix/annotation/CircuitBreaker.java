package com.cpp.cloud.hystrix.annotation;

import java.lang.annotation.*;

/**
 * 熔断注解
 *
 * @author chenjian
 * @date 2018-12-02 23:01
 */
@Target(ElementType.METHOD) // 标注在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时保存注解信息
@Documented
public @interface CircuitBreaker {

    /**
     * 超时时间
     *
     * @return
     */
    long timeout();

    /**
     * 并发数量
     *
     * @return
     */
    int concurrent();
}
