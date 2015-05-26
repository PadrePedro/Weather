package com.pedroid.weather.api;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by pedro on 5/21/15.
 *
 * Request processor that executes IRequest objects
 *
 */
public class RequestProcessor {

    public static final int THREADS_ONE = 1;
    public static final int THREADS_MANY = 4;

    public enum ThreadCount {ONE, MULTI};

    private static Executor executor;

    /**
     * Sets the number of threads for request processor, which defines how many
     * concurrent requests can be processed at any given time.  This must be called
     * before any requests are queued.
     *
     * @param threads # of threads
     */
    public static void setThreads(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    /**
     * Adds a request to the processor queue
     *
     * @param request Request to be processed
     */
    public static void execute(IRequest request) {
        if (executor == null) {
            throw new RuntimeException("RequestProcessor not initialized");
        }
        executor.execute(request);
    }
}
