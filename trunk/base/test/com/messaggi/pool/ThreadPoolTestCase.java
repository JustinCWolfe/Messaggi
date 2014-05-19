package com.messaggi.pool;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;

import com.messaggi.junit.MessaggiLogicTestCase;
import com.messaggi.pool.ThreadPoolTestHelper.TestingPoolThreads;
import com.messaggi.pool.ThreadPoolTestHelper.WaitingTask;
import com.messaggi.pool.task.Task;
import com.messaggi.pool.task.Task.State;

public class ThreadPoolTestCase<T extends ThreadPool> extends MessaggiLogicTestCase
{
    protected T pool;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        if (!pool.isShutdown()) {
            pool.shutdown();
        }
        if (!pool.isTerminated()) {
            pool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        }
        super.tearDown();
    }

    protected void validateWaitingTaskResults(WaitingTask... tasks)
    {
        for (WaitingTask t : tasks) {
            assertThat(t.getTaskResult(), equalTo(true));
        }
    }

    protected void validatePoolRunningState() throws Exception
    {
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertTrue(t.isAlive());
        }
    }

    protected void validateTaskInitialState(Task<?>... tasks) throws Exception
    {
        for (Task<?> task : tasks) {
            assertThat(task.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
            assertThat(task.getState(), equalTo(State.NONE));
        }
    }
}
