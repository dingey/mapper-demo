package com.d;

import com.github.dingey.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class FutureTaskMain {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<String> strings = Arrays.asList("a1", "a2");
        List<String> res = new ArrayList<>();
        long s1 = System.currentTimeMillis();
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
            try {
                res.add(future.get(3L, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        long s2 = System.currentTimeMillis();
        System.out.println("consume time " + (s2 - s1) / 1000d + " s");
        System.out.println(JsonUtil.toJson(res));
        executorService.shutdown();
    }
}
