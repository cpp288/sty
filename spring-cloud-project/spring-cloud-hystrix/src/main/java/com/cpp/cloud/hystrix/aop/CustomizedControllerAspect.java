package com.cpp.cloud.hystrix.aop;

import com.cpp.cloud.hystrix.annotation.CircuitBreaker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * CustomizedController Aspect
 *
 * @author chenjian
 * @date 2018-12-02 23:07
 */
@Aspect
@Component
public class CustomizedControllerAspect {

    /**
     * 通过线程池异步执行
     */
    private ExecutorService executorService = newFixedThreadPool(20);

    /**
     * 通过信号量的方式进行限流
     */
    private Semaphore semaphore = null;

    /**
     * 只切advancedSay方法
     *
     * @param point
     * @param circuitBreaker
     * @return
     */
    @Around("execution(* com.cpp.cloud.hystrix.controller.CustomizedController.advancedSay(..)) " +
            "&& @annotation(circuitBreaker)")
    public Object advancedSayAspect(ProceedingJoinPoint point, CircuitBreaker circuitBreaker) throws Exception {
        // 获取超时时间
        long timeout = circuitBreaker.timeout();
        // 获取并发数量
        int concurrent = circuitBreaker.concurrent();
        if (semaphore == null) {
            semaphore = new Semaphore(concurrent);
        }

        Object returnValue = null;
        try {
            if (semaphore.tryAcquire()) {
                Thread.sleep(1000);
                // 异步执行
                Future<Object> future = executorService.submit(() -> {
                    Object v = null;
                    try {
                        v = point.proceed(point.getArgs());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return v;
                });

                try {
                    returnValue = future.get(timeout, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    returnValue = "超时错误";
                }
            } else {
                returnValue = "限流错误";
            }
        } finally {
            // 信号量释放
            semaphore.release();
        }
        return returnValue;
    }
}
