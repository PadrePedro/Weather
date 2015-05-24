package com.pedroid.weather.api;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by pedro on 5/21/15.
 */
public class RequestProcessor {

    private static Executor executor = Executors.newFixedThreadPool(5);

    public static void setApiThreads(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    public static void execute(IRequest request) {
        executor.execute(request);
    }
}
