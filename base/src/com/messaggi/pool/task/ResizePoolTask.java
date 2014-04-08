package com.messaggi.pool.task;

import com.messaggi.pool.ThreadPool;

public class ResizePoolTask extends TaskBase
{
    private static final String NAME = "ResizePoolTask";

    private final ThreadPool pool;

    public ResizePoolTask(ThreadPool pool)
    {
        this.pool = pool;
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    protected void runInternal()
    {
    }
}

