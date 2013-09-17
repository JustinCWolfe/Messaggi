package com.messaggi.processor;

import java.util.Observer;

import com.messaggi.StepBase;
import com.messaggi.StepBase.Result;

public abstract class StepwiseProcessorBase<T, U> implements Observer
{
    private U data;

    private StepBase<T, U>[] steps;

    public void setStepData(U data)
    {
        this.data = data;
    }

    public void setSteps(StepBase<T, U>[] steps)
    {
        this.steps = steps;
    }

    public void processSteps()
    {
        for (StepBase<T, U> step : steps) {
            step.addObserver(this);
            try {
                Result result = step.process(data);
                if (result != Result.Continue) {
                    break;
                }
            } finally {
                step.deleteObserver(this);
            }
        }
    }
}

