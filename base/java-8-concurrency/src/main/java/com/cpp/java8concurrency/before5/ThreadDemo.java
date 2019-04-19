package com.cpp.java8concurrency.before5;

/**
 * java5之前的方式
 * 实现的局限性：
 *  - 缺少线程管理的原生支持（没有线程池）
 *  - 缺少"锁"的api（缺少Lock这样的api）
 *  - 缺少执行完成的原生支持
 *  - 执行结果获取困难
 *
 * @author chenjian
 * @version v1.0
 * @date 2018/11/23 4:43 PM
 */
public class ThreadDemo {

    public static void main(String[] args) {
        normalThread();
        completableThread();
    }

    /**
     * 普通线程
     */
    private static void normalThread() {
        Thread thread = new Thread(() -> System.out.printf("[Thread : %s]Hello World...\n", Thread.currentThread().getName()), "Sub");

        thread.start();

        System.out.printf("[Thread : %s]Starting...\n", Thread.currentThread().getName());
    }

    /**
     * 获取线程是否已经完成
     * 在获取 completableRunnable.isCompleted() 值时并不一定是true
     *      我们会想到可见性的问题，所以在 completed 字段加上 volatile 关键字
     *      但是还是会出现上面的问题，这里涉及到线程的执行顺序，当Sub线程还未执行到 completed = true; 时，主线程已经执行完了
     *      要解决这个问题需要使用 thread.join() 方法，主线程等待Sub线程执行完成
     */
    private static void completableThread() {
        CompletableRunnable completableRunnable = new CompletableRunnable();
        Thread thread = new Thread(completableRunnable, "Sub");

        thread.start();

//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.printf("[Thread : %s]Starting...\n", Thread.currentThread().getName());

        System.out.printf("runnable is completed : " + completableRunnable.isCompleted());
    }

    /**
     * 可完成的
     */
    private static class CompletableRunnable implements Runnable{

        private boolean completed = false;

        @Override
        public void run() {
            System.out.printf("[Thread : %s]Hello World...\n", Thread.currentThread().getName());
            completed = true;
        }

        public boolean isCompleted() {
            return completed;
        }
    }
}
