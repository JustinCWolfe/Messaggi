package com.messaggi.pool.task;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.messaggi.pool.AutoResizingThreadPool;
import com.messaggi.pool.task.InspectPoolQueueSizeTask.PoolSizeOpinion;

public class InspectPoolQueueSizeTask extends TaskBase<PoolSizeOpinion>
{
    public enum PoolSizeOpinion {
        NONE, OK, SHOULD_SHRINK, SHOULD_GROW, UNDECIDED, INTERRUPTED;
    }

    // This is accessed by unit tests so must be package protected scoped.
    protected static final int MILLISECONDS_BETWEEN_SAMPLES = 100;

    private static final String NAME = "InspectPoolQueueSizeTask";

    // This is accessed by unit tests so must be package protected scoped.
    protected static final int NUMBER_OF_SAMPLES = 3;

    private static final int POOL_SHOULD_GROW_COUNT = 100;

    private static final int POOL_SHOULD_SHRINK_COUNT = 5;

    private final Mean meanCalculator;

    private PoolSizeOpinion opinion = PoolSizeOpinion.NONE;

    private final AutoResizingThreadPool pool;

    private final StandardDeviation standardDeviationCalculator;

    public InspectPoolQueueSizeTask(AutoResizingThreadPool pool)
    {
        super();
        this.meanCalculator = new Mean();
        this.pool = pool;
        this.standardDeviationCalculator = new StandardDeviation();
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    protected PoolSizeOpinion getTaskResult()
    {
        return opinion;
    }

    @Override
    protected void runInternal()
    {
        try {
            opinion = PoolSizeOpinion.NONE;
            meanCalculator.clear();
            standardDeviationCalculator.clear();
            for (int sampleIndex = 1; sampleIndex <= NUMBER_OF_SAMPLES; sampleIndex++) {
                int taskCount = pool.getPoolTaskCount();
                System.out.println("Task count " + sampleIndex + ": " + taskCount);
                meanCalculator.increment(taskCount);
                standardDeviationCalculator.increment(taskCount);
                if (sampleIndex < NUMBER_OF_SAMPLES) {
                    Thread.sleep(MILLISECONDS_BETWEEN_SAMPLES);
                }
            }
            double mean = meanCalculator.getResult();
            double standardDeviation = standardDeviationCalculator.getResult();
            System.out.println("Mean: " + mean);
            System.out.println("StdDev: " + standardDeviation);
            if (mean != 0) {
                if (mean <= standardDeviation) {
                    opinion = PoolSizeOpinion.UNDECIDED;
                } else if (mean > standardDeviation) {
                    if (mean >= POOL_SHOULD_GROW_COUNT) {
                        opinion = PoolSizeOpinion.SHOULD_GROW;
                    } else if (mean <= POOL_SHOULD_SHRINK_COUNT) {
                        opinion = PoolSizeOpinion.SHOULD_SHRINK;
                    } else {
                        opinion = PoolSizeOpinion.OK;
                    }
                }
            }
        } catch (InterruptedException e) {
            opinion = PoolSizeOpinion.INTERRUPTED;
        }
    }
}

