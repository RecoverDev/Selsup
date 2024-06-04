package ru.selsup.dev;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ManagerPool<T> {
    private ThreadPoolExecutor pool;
    private List<Future<T>> results = null;
 
    public ManagerPool(TimeUnit timeUnit, int requestLimit) {
        pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        pool.setCorePoolSize(requestLimit);
        pool.setMaximumPoolSize(requestLimit);
        pool.setKeepAliveTime(1,timeUnit);
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

    }

    public void addTask(Callable<T> task) {
        if(results == null) {
            results = new LinkedList<>();
        }
        results.add(pool.submit(task));
    }

    public void giveResult(Visitor<T> visitor) {

        List<T> outResults = new LinkedList<>();
        for (Future<T> f : results) {
            try {
                outResults.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Ошибка получения результата задания - " + e.getMessage());
            }
        }
        visitor.out(outResults);
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
        }
    }
}
