package com.messaggi.pool.task;

public class EndOfStreamTask extends TaskBase<Void>
{
    public static final EndOfStreamTask POISON = new EndOfStreamTask();

    private static final String NAME = "EndOfStreamTask";

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    protected void runInternal()
    {
        throw new RuntimeException("Threads should die now");
    }
}

