package com.messaggi.pool.task;

import java.util.concurrent.TimeUnit;

public interface Task extends Runnable
{
    public enum State {
        NONE, STARTED, COMPLETED;
    }

    String getName();

    State getState();

    long getTotalRunTime(TimeUnit timeUnit);
}

