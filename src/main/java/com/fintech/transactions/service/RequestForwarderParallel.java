package com.fintech.transactions.service;

import com.fintech.transactions.model.Transaction;
import com.fintech.transactions.service.anomaly.AnomalyTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Creates a threadpool ahead of time and accepts new transactions to be passed on to anomaly service in a multi-threaded way
 */
public class RequestForwarderParallel {

    private static ExecutorService executor;

    public static void checkForAnamolies(Transaction transaction) {
        //using submit here because it runs the task and saves the result in form of Future object
        if (executor == null) init();

        Future<Boolean> future = executor.submit(new AnomalyTask(transaction));

        //read from future object if needed.Skipping for now.
    }

    public static void init() {
        executor = Executors.newCachedThreadPool();
    }
}
