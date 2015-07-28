package com.messaggi.pool;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.junit.MessaggiLogicTestCase;
import com.messaggi.pool.task.LeastSquaresResizing;
import com.messaggi.pool.task.QueueStatistics.QueueStatistic;

public class TestLeastSquaresResizing extends MessaggiLogicTestCase
{
    private static final QueueStatistic EMPTY_STATISTIC = new QueueStatistic();

    private enum CurrentThreadCount
    {
        NONE(0), MEDIUM(10), LARGE(100);

        private final int threadCount;

        private CurrentThreadCount(int threadCount)
        {
            this.threadCount = threadCount;
        }
    }

    private LeastSquaresResizing resizing;

    private QueueStatistic steadyStateStatistic;

    private QueueStatistic increasingStatistic;

    private QueueStatistic decreasingStatistic;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        resizing = new LeastSquaresResizing();
        steadyStateStatistic = new QueueStatistic();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testWithNoStatistics(CurrentThreadCount currentThreadCount)
    {
        int newThreadCount = resizing.computeThreadCount(currentThreadCount.threadCount);
        assertThat(newThreadCount, equalTo(currentThreadCount.threadCount));
    }

    @Test
    public void testWithNoStatistics_NoneCurrentThreadCount()
    {
        testWithNoStatistics(CurrentThreadCount.NONE);
    }

    @Test
    public void testWithNoStatistics_MediumCurrentThreadCount()
    {
        testWithNoStatistics(CurrentThreadCount.MEDIUM);
    }

    @Test
    public void testWithNoStatistics_LargeCurrentThreadCount()
    {
        testWithNoStatistics(CurrentThreadCount.LARGE);
    }

    public void testWithEmptyStatistic(CurrentThreadCount currentThreadCount)
    {
        resizing.addStatistic(EMPTY_STATISTIC);
        int newThreadCount = resizing.computeThreadCount(currentThreadCount.threadCount);
        assertThat(newThreadCount, equalTo(currentThreadCount.threadCount));
    }

    @Test
    public void testWithEmptyStatistic_NoneCurrentThreadCount()
    {
        testWithEmptyStatistic(CurrentThreadCount.NONE);
    }

    @Test
    public void testWithEmptyStatistic_MediumCurrentThreadCount()
    {
        testWithEmptyStatistic(CurrentThreadCount.MEDIUM);
    }

    @Test
    public void testWithEmptyStatistic_LargeCurrentThreadCount()
    {
        testWithEmptyStatistic(CurrentThreadCount.LARGE);
    }
}

