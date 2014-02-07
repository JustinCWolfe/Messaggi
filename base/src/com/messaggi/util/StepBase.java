package com.messaggi.util;

import java.util.Observable;

public abstract class StepBase<T, U> extends Observable
{
    public enum Result {
        Continue, Stop;
    }

    private T statistics;
    
    public T getStatistics()
    {
        return statistics;
    }

    public boolean getStepWasProcessed()
    {
        return statistics != null;
    }

    public abstract Result process(U data);

    @Override
    public void notifyObservers(Object arg)
    {
        // Toggle the changed flag for this object so that the base notifyObservers
        // will propagate our message to observers.
        setChanged();
        super.notifyObservers(arg);
    }
}

