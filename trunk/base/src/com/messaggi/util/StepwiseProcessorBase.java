package com.messaggi.util;

import java.util.Observer;

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
                StepBase.Result result = step.process(data);
                if (result != StepBase.Result.Continue) {
                    break;
                }
            } finally {
                step.deleteObserver(this);
            }
        }
    }
}

