package com.messaggi.pool.task;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public abstract class TaskBase<T> implements Task
{
    public interface ReceiveResult<T>
    {
        void receiveTaskResult(T result);
    }

    private final ReceiveResult<T> resultCallback;

    private State state = State.NONE;

    private final Stopwatch stopwatch;

    private long totalMilliseconds;

    protected TaskBase()
    {
        this(null);
    }

    protected TaskBase(ReceiveResult<T> resultCallback)
    {
        this.resultCallback = resultCallback;
        this.stopwatch = Stopwatch.createUnstarted();
    }

    protected T getTaskResult()
    {
        return null;
    }

    protected abstract void runInternal();

    @Override
    public void run()
    {
        state = State.STARTED;
        stopwatch.reset();
        stopwatch.start();
        runInternal();
        if (resultCallback != null) {
            T result = getTaskResult();
            resultCallback.receiveTaskResult(result);
        }
        stopwatch.stop();
        totalMilliseconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        state = State.COMPLETED;
    }

    @Override
    public State getState()
    {
        return state;
    }

    @Override
    public long getTotalRunTime(TimeUnit timeUnit)
    {
        return timeUnit.convert(totalMilliseconds, timeUnit);
    }
}

