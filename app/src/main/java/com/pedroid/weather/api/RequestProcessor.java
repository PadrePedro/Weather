package com.pedroid.weather.api;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by pedro on 5/21/15.
 */
public class RequestProcessor {

    public static final int THREADS_ONE = 1;
    public static final int THREADS_MANY = 4;

    public enum ThreadCount {ONE, MULTI};


    private static Executor executor;

    public static void setThreads(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    public static void execute(IRequest request) {
        if (executor == null) {
            throw new RuntimeException("RequestProcessor not initialized");
        }
        executor.execute(request);
    }
}
