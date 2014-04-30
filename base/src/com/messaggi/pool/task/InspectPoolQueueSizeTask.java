package com.messaggi.pool.task;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.messaggi.pool.task.InspectPoolQueueSizeTask.PoolSizeOpinion;

public class InspectPoolQueueSizeTask extends TaskBase<PoolSizeOpinion>
{
    public enum PoolSizeOpinion {
        NONE, OK, SHOULD_SHRINK, SHOULD_GROW, UNDECIDED, INTERRUPTED;
    }

    public interface InspectablePool
    {
        int getPoolTaskCount();

        long getSecondsBetweenPoolSizeInspections();
    }

    private static final String NAME = "InspectPoolQueueSizeTask";

    // This is accessed by unit tests so must be package protected scoped.
    protected static final int NUMBER_OF_SAMPLES = 3;

    // This is accessed by unit tests so must be public scoped.
    public static final int POOL_SHOULD_GROW_COUNT = 100;

    // This is accessed by unit tests so must be public scoped.
    public static final int POOL_SHOULD_SHRINK_COUNT = 5;

    private final Mean meanCalculator;

    private final long millisecondsBetweenSamples;

    private PoolSizeOpinion opinion = PoolSizeOpinion.NONE;

    private final InspectablePool pool;

    private final StandardDeviation standardDeviationCalculator;

    public InspectPoolQueueSizeTask(InspectablePool pool)
    {
        super();
        this.meanCalculator = new Mean();
        // Seconds * 1000 to get milliseconds * .5% (or .005) to get sample time based on inspection interval.
        this.millisecondsBetweenSamples = pool.getSecondsBetweenPoolSizeInspections() * 5;
        this.pool = pool;
        this.standardDeviationCalculator = new StandardDeviation();
    }

    public long getMillisecondsBetweenSamples()
    {
        return millisecondsBetweenSamples;
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
                    Thread.sleep(millisecondsBetweenSamples);
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
            // Reset interrupt flag.
            Thread.currentThread().interrupt();
            opinion = PoolSizeOpinion.INTERRUPTED;
        }
    }
}

