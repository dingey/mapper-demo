package com.d.ft;

import com.github.dingey.common.util.JsonUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParallelTaskExecutors {

    public static <Param, Key, Value> Map<Key, ParallelResult<Value>> syncExecute(
            Map<Key, ParallelTask<Param, Value>> parallelTaskMap,
            Executor executor, int timeoutSecond, Param param) {
        Map<Key, ParallelResult<Value>> resultMap = new ConcurrentHashMap<>();
        Map<Key, String> consumeTimeMap = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(parallelTaskMap.size());
        for (Map.Entry<Key, ParallelTask<Param, Value>> entry : parallelTaskMap.entrySet()) {
            executor.execute(() -> {
                long start = System.currentTimeMillis();
                resultMap.put(entry.getKey(), new ParallelResult<Value>().setStatus(ParallelStatus.timeout));
                try {
                    ParallelResult<Value> result = entry.getValue().call(param);
                    resultMap.put(entry.getKey(), result);
                } catch (Exception e) {
                    e.printStackTrace();
                    resultMap.put(entry.getKey(), new ParallelResult<Value>()
                            .setStatus(ParallelStatus.exception));
                } finally {
                    latch.countDown();
                    long end = System.currentTimeMillis();
                    consumeTimeMap.put(entry.getKey(), String.format("%dms", end - start));
                    System.out.println(String.format("thread %s run %d ms", Thread.currentThread().getName(), end - start));
                }
            });
        }
        try {
            latch.await(timeoutSecond, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("consume time : " + JsonUtil.toJson(consumeTimeMap));
        return resultMap;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Integer> integers = Arrays.asList(3, 5, 4, 2, 8, 1);

        Map<String, ParallelTask<Integer, String>> map = new HashMap<>();
        for (Integer i : integers) {
            map.put("a" + i, new ParallelTask<Integer, String>() {
                @Override
                ParallelResult<String> call(Integer integer) {
                    try {
                        TimeUnit.SECONDS.sleep(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return ParallelResult.success("V" + integer);
                }
            });
        }

        Map<String, ParallelResult<String>> resultMap = syncExecute(map, executorService, 4, 777);
        System.out.println(JsonUtil.toJson(resultMap));

        executorService.shutdown();
    }
}
