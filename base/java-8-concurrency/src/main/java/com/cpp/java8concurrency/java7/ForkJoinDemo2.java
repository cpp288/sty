package com.cpp.java8concurrency.java7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * 计算整数之和
 *
 * @author chenjian
 * @version v1.0
 * @date 2018/11/26 9:52 AM
 */
public class ForkJoinDemo2 {

    public static void main(String[] args) {

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        LongAccumulator accumulator = new LongAccumulator(((left, right) -> left + right), 0);

        List<Long> params = new ArrayList<>();
        for (long i = 0; i < 10000000; i++) {
            params.add(i);
        }

        long start = System.currentTimeMillis();
        forkJoinPool.invoke(new LongSumTask(params, accumulator));
        long end = System.currentTimeMillis();

        System.out.println(accumulator.get());
        System.out.printf("消耗时间：%d %s\n", end - start, "ms");

        forkJoinPool.shutdown();
    }

    static class LongSumTask extends RecursiveAction {

        private final List<Long> elements;

        private final LongAccumulator accumulator;

        LongSumTask(List<Long> elements, LongAccumulator accumulator) {
            this.elements = elements;
            this.accumulator = accumulator;
        }

        @Override
        public void compute() {

            int size = elements.size();

            int parts = size / 2;

            // 使用简单的二分法，将计算平分，当元素只有一个的时候使用 LongAccumulator 进行累加计算
            if (size > 1) {

                List<Long> left = elements.subList(0, parts);
                List<Long> right = elements.subList(parts, size);

                new LongSumTask(left, accumulator).fork().join();
                new LongSumTask(right, accumulator).fork().join();

            } else {

                if (elements.isEmpty()) {
                    return;
                }

                Long num = elements.get(0);
                accumulator.accumulate(num);

            }

        }

    }

}
