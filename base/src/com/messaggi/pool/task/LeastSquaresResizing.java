package com.messaggi.pool.task;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.messaggi.pool.task.QueueStatistics.QueueResizingStrategy;
import com.messaggi.pool.task.QueueStatistics.QueueStatistic;

public class LeastSquaresResizing implements QueueResizingStrategy
{
    private final ConcurrentLinkedQueue<QueueStatistic> statistics = new ConcurrentLinkedQueue<>();

    @Override
    public void addStatistic(QueueStatistic statistic)
    {
        statistics.add(statistic);
    }

    @Override
    public int computeThreadCount(int currentThreadCount)
    {
        int sumProcessed = 0;
        int time = 0;
        int sumTime = 0;
        int sumLength = 0;
        double sumTimeMultByLength = 0;
        double sumTimeSquared = 0;
        for (QueueStatistic statistic : statistics) {
            time++;
            sumTime += time;
            sumLength += statistic.getLength();
            sumTimeMultByLength += time * statistic.getLength();
            sumTimeSquared += Math.pow(time, 2);
            sumProcessed += statistic.getProcessedCount();
        }
        int statisticsCount = time;
        if (statisticsCount == 0) {
            return currentThreadCount;
        }
        double slope = (statisticsCount * sumTimeMultByLength - sumTime * sumLength)
                / statisticsCount * sumTimeSquared - Math.pow(sumTime, 2);
        double meanProcessed = sumProcessed / statisticsCount;
        int threadCountMultiplier = (int) (slope / meanProcessed);
        return currentThreadCount + (currentThreadCount * threadCountMultiplier);
    }
}

