package com.messaggi.pool.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public abstract class TaskBase<T> implements Task<T>
{
    private final BlockingQueue<T> resultHolder;

    private State state = State.NONE;

    private final Stopwatch stopwatch;

    private long totalMilliseconds;

    protected TaskBase()
    {
        //TODO: investigate other types to use instead of this one.
        this.resultHolder = new LinkedBlockingQueue<>(1);
        this.stopwatch = Stopwatch.createUnstarted();
    }

    @Override
    public T getResult()
    {
        //TODO: investigate proper way to handle interrupted exception.
        for (;;) {
            try {
                return resultHolder.take();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public State getState()
    {
        return state;
    }

    protected T getTaskResult()
    {
        return null;
    }

    @Override
    public long getTotalRunTime(TimeUnit timeUnit)
    {
        return timeUnit.convert(totalMilliseconds, timeUnit);
    }

    protected abstract void runInternal();

    @Override
    public void run()
    {
        resultHolder.clear();
        state = State.STARTED;
        stopwatch.reset();
        stopwatch.start();
        runInternal();
        T taskResult = getTaskResult();
        if (taskResult != null) {
            //TODO: investigate proper way to handle interrupted exception.
            for (;;) {
                try {
                    resultHolder.put(taskResult);
                    break;
                } catch (InterruptedException e) {

                }
            }
        }
        stopwatch.stop();
        totalMilliseconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        state = State.COMPLETED;
    }
}

