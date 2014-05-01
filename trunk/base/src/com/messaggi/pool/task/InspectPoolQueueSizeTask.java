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

    private static final int CHECK_MEAN_VERSUS_STANDARD_DEVIATION_MEAN_LIMIT = 10;

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
            setOpinionBasedOnMeanAndStandardDeviation(mean, standardDeviation);
        } catch (InterruptedException e) {
            // Reset interrupt flag.
            Thread.currentThread().interrupt();
            opinion = PoolSizeOpinion.INTERRUPTED;
        }
    }

    private boolean shouldCalculateOpinionBasedOnMean(double mean, double standardDeviation)
    {
        // With small numbers of tasks in the pool, it is very easy for the standard
        // deviation to be larger than the mean, which would get us stuck in a state
        // where we could get repeated undecided results. To guard against this, only
        // do the mean vs standard deviation check if the mean is less than a set number.
        if (mean > CHECK_MEAN_VERSUS_STANDARD_DEVIATION_MEAN_LIMIT) {
            return (mean > standardDeviation);
        }
        return true;
    }

    private PoolSizeOpinion calculateOpinionBasedOnMean(double mean)
    {
        if (mean >= POOL_SHOULD_GROW_COUNT) {
            return PoolSizeOpinion.SHOULD_GROW;
        } else if (mean <= POOL_SHOULD_SHRINK_COUNT) {
            return PoolSizeOpinion.SHOULD_SHRINK;
        } else {
            return PoolSizeOpinion.OK;
        }
    }

    private void setOpinionBasedOnMeanAndStandardDeviation(double mean, double standardDeviation)
    {
        if (mean != 0) {
            if (shouldCalculateOpinionBasedOnMean(mean, standardDeviation)) {
                opinion = calculateOpinionBasedOnMean(mean);
            } else {
                opinion = PoolSizeOpinion.UNDECIDED;
            }
        }
    }
}

