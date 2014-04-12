package com.messaggi.pool.task;

import java.util.concurrent.TimeUnit;

public interface Task<T extends Object> extends Runnable
{
    public enum State {
        NONE, STARTED, COMPLETED;
    }

    /**
     * Wait for task to run and get its result (lightweight Future<T>).
     */
    T getResult();

    String getName();

    State getState();

    long getTotalRunTime(TimeUnit timeUnit);
}

