package com.d;

import com.github.dingey.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureTaskMain2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<String> strings = Arrays.asList("a1", "a2");

        long s1 = System.currentTimeMillis();
        List<Future<String>> futures = new ArrayList<>();
        for (String string : strings) {
            Future<String> future = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    long start = System.currentTimeMillis();
                    try {
                        int i = new Random().nextInt(8);
                        System.out.println("sleep " + i + " s");
                        TimeUnit.SECONDS.sleep(i);
                        return string + " = " + i;
                    } finally {
                        long end = System.currentTimeMillis();
                        System.out.println(String.format("thread %s run %d ms", Thread.currentThread().getName(), end - start));
                    }
                }
            });
            futures.add(future);
        }
        List<String> res = futures.parallelStream().map(f -> {
            try {
                return f.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            } catch (TimeoutException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        long s2 = System.currentTimeMillis();
        System.out.println("consume time " + (s2 - s1) / 1000d + " s");
        System.out.println(JsonUtil.toJson(res));
        executorService.shutdown();
    }
}
