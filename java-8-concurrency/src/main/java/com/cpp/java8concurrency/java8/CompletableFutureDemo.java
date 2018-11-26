package com.cpp.java8concurrency.java8;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * {@link CompletableFuture} Demo
 *
 * @author chenjian
 * @version v1.0
 * @date 2018/11/26 10:51 AM
 * @see java.util.concurrent.CompletionStage
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. 完成操作（可以被其它线程去做）
//        CompletableFuture<String> completableFuture = new CompletableFuture<>();
//        completableFuture.complete("Hello World");
//        String v = completableFuture.get();
//        System.out.println(v);

        // 2. runAsync 异步执行，阻塞操作
//        CompletableFuture asyncCompletableFuture = CompletableFuture.runAsync(() -> {
//            System.out.printf("[Thread : %s]Hello World...\n", Thread.currentThread().getName());
//        });
//
//        // 这里仍然是阻塞的
//        asyncCompletableFuture.get();
//
//        System.out.println("Starting...");

        // 3. supplyAsync 异步执行，阻塞操作
//        CompletableFuture<String> asyncCompletableFuture = CompletableFuture.supplyAsync(() -> {
//            // 获取数据操作，比如来自于数据库
//            return String.format("[Thread : %s]Hello World...\n", Thread.currentThread().getName());
//        });
//
//        String v = asyncCompletableFuture.get();
//        System.out.println(v);
//        System.out.println("Starting...");

        // 4. 合并操作
        CompletableFuture<String> combinedCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 获取数据操作，比如来自于数据库
            return String.format("[Thread : %s]Hello World...", Thread.currentThread().getName());
        }).thenApply(value -> {
            System.out.printf("current thread : %s\n", Thread.currentThread().getName());
            return value + " - 来自于数据库";
        }).thenApplyAsync(value -> {
            System.out.printf("current thread : %s\n", Thread.currentThread().getName());
            return value + " at " + LocalDate.now();
        }).exceptionally(e -> {
            // 异常处理
            e.printStackTrace();
            return "";
        });

        while (!combinedCompletableFuture.isDone()) {

        }

        String v = combinedCompletableFuture.get();
        System.out.println(v);

        System.out.println("Starting...");

    }
}
