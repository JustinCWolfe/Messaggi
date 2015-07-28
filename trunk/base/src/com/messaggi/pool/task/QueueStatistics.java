package com.messaggi.pool.task;

import java.util.concurrent.atomic.AtomicInteger;

public class QueueStatistics
{
    public static class QueueStatistic
    {
        private final AtomicInteger length = new AtomicInteger();

        private final AtomicInteger addedCount = new AtomicInteger();

        private final AtomicInteger processedCount = new AtomicInteger();

        public int incrementLength()
        {
            return length.incrementAndGet();
        }

        public int incrementAddedCount()
        {
            return addedCount.incrementAndGet();
        }

        public int incrementProcessedCount()
        {
            return processedCount.incrementAndGet();
        }

        public int getLength()
        {
            return length.get();
        }

        public int getAddedCount()
        {
            return addedCount.get();
        }

        public int getProcessedCount()
        {
            return processedCount.get();
        }
    }

    public static interface QueueResizingStrategy
    {
        void addStatistic(QueueStatistic statistic);

        int computeThreadCount(int currentThreadCount);
    }
    
    public static class QueueResizingFactory
    {
        public enum ResizingStrategy
        {
            LEAST_SQUARES;
        }

        public QueueResizingStrategy create(ResizingStrategy strategy)
        {
            switch (strategy) {
                case LEAST_SQUARES:
                    return new LeastSquaresResizing();
                default:
                    return null;
            }
        }
    }
}

