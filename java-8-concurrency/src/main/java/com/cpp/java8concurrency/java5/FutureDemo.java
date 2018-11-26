package com.cpp.java8concurrency.java5;

import java.util.concurrent.*;

/**
 * Callable是有返回值操作，相对于Runnable
 *
 * Future的限制
 *      - 无法手动完成
 *      - 阻塞式结果返回 future.get()
 *      - 无法链式调用多个Future，从 ExecutorService#invokeAll 方法中只能返回Future的集合
 *      - 无法合并多个Future的结果，从 ExecutorService#invokeAll 方法中只能返回Future的集合
 *      - 缺少异常处理
 *
 * @author chenjian
 * @version v1.0
 * @date 2018/11/26 9:11 AM
 * @see Runnable
 * @see Callable
 */
public class FutureDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "[Thread : " + Thread.currentThread().getName() + "]Hello World...";
            }
        });

        // 可以知道该线程是否执行完成
//        future.isDone();

        try {
            String v = future.get();
            System.out.println(v);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
