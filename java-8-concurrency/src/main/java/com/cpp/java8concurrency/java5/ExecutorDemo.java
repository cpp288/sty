package com.cpp.java8concurrency.java5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executor
 *
 * @author chenjian
 * @version v1.0
 * @date 2018/11/23 5:26 PM
 */
public class ExecutorDemo {

    public static void main(String[] args) {

        // 执行器服务，线程池 ThreadPoolExecutor 是它的一种实现
        ExecutorService executor = Executors.newFixedThreadPool(1);

        executor.execute(() -> System.out.printf("[Thread : %s]Hello World...\n", Thread.currentThread().getName()));

        // 合理的关闭线程池是非常重要的
        executor.shutdown();
    }
}
