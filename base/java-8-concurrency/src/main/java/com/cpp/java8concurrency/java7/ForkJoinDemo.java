package com.cpp.java8concurrency.java7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * Fork/Join 示例
 *
 * @author chenjian
 * @version v1.0
 * @date 2018/11/26 9:29 AM
 */
public class ForkJoinDemo {

    public static void main(String[] args) {

        // ForkJoinPool 线程池，用于并行计算
        System.out.printf("当前公用 ForkJoinPool 线程池并行数：%d\n", ForkJoinPool.commonPool().getParallelism());
        System.out.printf("当前CPU处理器数：%d\n", Runtime.getRuntime().availableProcessors());

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 有两种方式：1. ForkJoinTask 2. RecursiveAction
//        forkJoinPool.invoke(new ForkJoinTask<String>() {
//
//            @Override
//            public String getRawResult() {
//                return null;
//            }
//
//            @Override
//            protected void setRawResult(String value) {
//
//            }
//
//            @Override
//            protected boolean exec() {
//                return false;
//            }
//        });

        forkJoinPool.invoke(new RecursiveAction() {
            @Override
            protected void compute() {
                System.out.printf("[Thread : %s]Hello World...\n", Thread.currentThread().getName());
            }
        });

        forkJoinPool.shutdown();
    }
}
