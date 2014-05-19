package com.messaggi.pool;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.messaggi.pool.ThreadPoolTestHelper.PoolAddTaskCaller;
import com.messaggi.pool.ThreadPoolTestHelper.PoolAwaitTerminationCaller;
import com.messaggi.pool.ThreadPoolTestHelper.PoolShutdownCaller;
import com.messaggi.pool.ThreadPoolTestHelper.TestingPoolThreads;
import com.messaggi.pool.ThreadPoolTestHelper.WaitingTask;
import com.messaggi.pool.task.Task.State;

public class TestThreadPool extends ThreadPoolTestCase<ThreadPool>
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        pool = new ThreadPool();
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

    @Test
    public void testAddTask() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6 };
        validateTaskInitialState(tasks);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        pool.addTask(t4);
        pool.addTask(t5);
        pool.addTask(t6);
        assertThat(t1.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t2.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        // After 2 seconds, t1 and t2 should have completed.
        Thread.sleep(waitTime * 1 + 100);
        assertThat(t1.getState(), equalTo(State.COMPLETED));
        assertThat(t2.getState(), equalTo(State.COMPLETED));
        assertThat((double) t1.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat(t3.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t4.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        // After 4 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime * 2 + 100);
        assertThat(t3.getState(), equalTo(State.COMPLETED));
        assertThat(t4.getState(), equalTo(State.COMPLETED));
        assertThat((double) t3.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat(t5.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t6.getState(), not(equalTo(State.COMPLETED)));
        // After 6 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime * 3 + 100);
        assertThat(t5.getState(), equalTo(State.COMPLETED));
        assertThat(t6.getState(), equalTo(State.COMPLETED));
        assertThat((double) t5.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        assertThat((double) t6.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testAddTaskInternal_AddToFront() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6 };
        validatePoolRunningState();
        validateTaskInitialState(tasks);
        pool.addTaskInternal(t1, true);
        pool.addTaskInternal(t2, true);
        pool.addTaskInternal(t3, true);
        pool.addTaskInternal(t4, true);
        pool.addTaskInternal(t5, true);
        pool.addTaskInternal(t6, true);
        Thread.sleep(20);
        assertThat(t1.getState(), equalTo(State.NONE));
        assertThat(t2.getState(), equalTo(State.NONE));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.STARTED));
        assertThat(t6.getState(), equalTo(State.STARTED));
        // After 2 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime * 3 + 20);
        assertThat(t5.getState(), equalTo(State.COMPLETED));
        assertThat(t6.getState(), equalTo(State.COMPLETED));
        assertThat((double) t5.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 20));
        assertThat((double) t6.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 20));
        assertThat(t1.getState(), equalTo(State.NONE));
        assertThat(t2.getState(), equalTo(State.NONE));
        assertThat(t3.getState(), equalTo(State.STARTED));
        assertThat(t4.getState(), equalTo(State.STARTED));
        // After 4 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime * 2 + 20);
        assertThat(t3.getState(), equalTo(State.COMPLETED));
        assertThat(t4.getState(), equalTo(State.COMPLETED));
        assertThat((double) t3.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat(t1.getState(), equalTo(State.STARTED));
        assertThat(t2.getState(), equalTo(State.STARTED));
        // After 6 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime * 1 + 20);
        assertThat(t1.getState(), equalTo(State.COMPLETED));
        assertThat(t2.getState(), equalTo(State.COMPLETED));
        assertThat((double) t1.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testAddTask_MultipleThreads() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime);
        WaitingTask t2 = new WaitingTask(waitTime);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        WaitingTask t7 = new WaitingTask(waitTime * 4);
        WaitingTask t8 = new WaitingTask(waitTime * 4);
        WaitingTask t9 = new WaitingTask(waitTime * 5);
        WaitingTask t10 = new WaitingTask(waitTime * 5);
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 };
        validatePoolRunningState();
        validateTaskInitialState(tasks);
        PoolAddTaskCaller r1 = new PoolAddTaskCaller(pool, false, t1);
        PoolAddTaskCaller r2 = new PoolAddTaskCaller(pool, false, t2);
        PoolAddTaskCaller r3 = new PoolAddTaskCaller(pool, false, t3);
        PoolAddTaskCaller r4 = new PoolAddTaskCaller(pool, false, t4);
        PoolAddTaskCaller r5 = new PoolAddTaskCaller(pool, false, t5);
        PoolAddTaskCaller r6 = new PoolAddTaskCaller(pool, false, t6);
        PoolAddTaskCaller r7 = new PoolAddTaskCaller(pool, false, t7);
        PoolAddTaskCaller r8 = new PoolAddTaskCaller(pool, false, t8);
        PoolAddTaskCaller r9 = new PoolAddTaskCaller(pool, false, t9);
        PoolAddTaskCaller r10 = new PoolAddTaskCaller(pool, false, t10);
        List<Thread> testThreads1 = ThreadPoolTestHelper.getTestThreads(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        for (Thread t : testThreads1) {
            t.start();
            Thread.sleep(10);
        }
        for (Thread t : testThreads1) {
            t.join();
        }
        // After 0 seconds, no tasks should have completed.
        assertThat(t1.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t2.getState(), not(equalTo(State.COMPLETED)));
        // These tasks may have started - it is a race so check both valid cases.
        assertThat(t3.getState(), anyOf(equalTo(State.NONE), equalTo(State.STARTED)));
        assertThat(t4.getState(), anyOf(equalTo(State.NONE), equalTo(State.STARTED)));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 2 seconds, t1 and t2 should have completed.
        Thread.sleep(waitTime * 1 + 100);
        assertThat(t1.getState(), equalTo(State.COMPLETED));
        assertThat(t2.getState(), equalTo(State.COMPLETED));
        assertThat((double) t1.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat(t3.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t4.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 4 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime * 2 + 100);
        assertThat(t3.getState(), equalTo(State.COMPLETED));
        assertThat(t4.getState(), equalTo(State.COMPLETED));
        assertThat((double) t3.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat(t5.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t6.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 6 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime * 3 + 100);
        assertThat(t5.getState(), equalTo(State.COMPLETED));
        assertThat(t6.getState(), equalTo(State.COMPLETED));
        assertThat((double) t5.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        assertThat((double) t6.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        assertThat(t7.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t8.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 8 seconds, t7 and t8 should have completed.
        Thread.sleep(waitTime * 4 + 100);
        assertThat(t7.getState(), equalTo(State.COMPLETED));
        assertThat(t8.getState(), equalTo(State.COMPLETED));
        assertThat((double) t7.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 4, 100));
        assertThat((double) t8.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 4, 100));
        assertThat(t9.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t10.getState(), not(equalTo(State.COMPLETED)));
        // After 10 seconds, t9 and t10 should have completed.
        Thread.sleep(waitTime * 5 + 100);
        assertThat(t9.getState(), equalTo(State.COMPLETED));
        assertThat(t10.getState(), equalTo(State.COMPLETED));
        assertThat((double) t9.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 5, 100));
        assertThat((double) t10.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 5, 100));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testAddTaskInternal_AddToFront_MultipleThreads() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime);
        WaitingTask t2 = new WaitingTask(waitTime);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        WaitingTask t7 = new WaitingTask(waitTime * 4);
        WaitingTask t8 = new WaitingTask(waitTime * 4);
        WaitingTask t9 = new WaitingTask(waitTime * 5);
        WaitingTask t10 = new WaitingTask(waitTime * 5);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 };
        validateTaskInitialState(tasks);
        PoolAddTaskCaller r1 = new PoolAddTaskCaller(pool, true, t1);
        PoolAddTaskCaller r2 = new PoolAddTaskCaller(pool, true, t2);
        PoolAddTaskCaller r3 = new PoolAddTaskCaller(pool, true, t3);
        PoolAddTaskCaller r4 = new PoolAddTaskCaller(pool, true, t4);
        PoolAddTaskCaller r5 = new PoolAddTaskCaller(pool, true, t5);
        PoolAddTaskCaller r6 = new PoolAddTaskCaller(pool, true, t6);
        PoolAddTaskCaller r7 = new PoolAddTaskCaller(pool, true, t7);
        PoolAddTaskCaller r8 = new PoolAddTaskCaller(pool, true, t8);
        PoolAddTaskCaller r9 = new PoolAddTaskCaller(pool, true, t9);
        PoolAddTaskCaller r10 = new PoolAddTaskCaller(pool, true, t10);
        List<Thread> testThreads1 = ThreadPoolTestHelper.getTestThreads(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        for (Thread t : testThreads1) {
            t.start();
            Thread.sleep(5);
        }
        for (Thread t : testThreads1) {
            t.join();
        }
        // After 0 seconds, no tasks should have completed.
        assertThat(t1.getState(), equalTo(State.STARTED));
        assertThat(t2.getState(), equalTo(State.STARTED));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 2 seconds, t9 and t10 should have completed.
        Thread.sleep(waitTime * 1 + 10);
        assertThat(t1.getState(), equalTo(State.COMPLETED));
        assertThat(t2.getState(), equalTo(State.COMPLETED));
        assertThat((double) t1.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.STARTED));
        assertThat(t10.getState(), equalTo(State.STARTED));
        // After 4 seconds, t7 and t8 should have completed.
        Thread.sleep(waitTime * 5 + 10);
        assertThat(t9.getState(), equalTo(State.COMPLETED));
        assertThat(t10.getState(), equalTo(State.COMPLETED));
        assertThat((double) t9.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 5, 100));
        assertThat((double) t10.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 5, 100));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.STARTED));
        assertThat(t8.getState(), equalTo(State.STARTED));
        // After 6 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime * 4 + 10);
        assertThat(t7.getState(), equalTo(State.COMPLETED));
        assertThat(t8.getState(), equalTo(State.COMPLETED));
        assertThat((double) t7.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 4, 100));
        assertThat((double) t8.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 4, 100));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.STARTED));
        assertThat(t6.getState(), equalTo(State.STARTED));
        // After 8 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime * 3 + 10);
        assertThat(t5.getState(), equalTo(State.COMPLETED));
        assertThat(t6.getState(), equalTo(State.COMPLETED));
        assertThat((double) t5.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        assertThat((double) t6.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        assertThat(t3.getState(), equalTo(State.STARTED));
        assertThat(t4.getState(), equalTo(State.STARTED));
        // After 10 seconds, t1 and t2 should have completed.
        Thread.sleep(waitTime * 2 + 10);
        assertThat(t3.getState(), equalTo(State.COMPLETED));
        assertThat(t4.getState(), equalTo(State.COMPLETED));
        assertThat((double) t3.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testAddTaskIntoShutdownPool() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4 };
        validateTaskInitialState(tasks);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        validatePoolRunningState();
        pool.shutdown();
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertTrue(t.isAlive());
        }
        try {
            pool.addTask(t4);
            fail("Should not get here");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("Attempting to assign work to shutdown thread pool."));
            assertThat(t1.getState(), not(equalTo(State.COMPLETED)));
            assertThat(t2.getState(), not(equalTo(State.COMPLETED)));
            assertThat(t3.getState(), equalTo(State.NONE));
            assertThat(t4.getState(), equalTo(State.NONE));
            // After 2 seconds, t1 and t2 should have completed.
            Thread.sleep(waitTime * 1 + 100);
            assertThat(t1.getState(), equalTo(State.COMPLETED));
            assertThat(t2.getState(), equalTo(State.COMPLETED));
            assertThat((double) t1.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
            assertThat((double) t2.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
            assertThat(t3.getState(), not(equalTo(State.COMPLETED)));
            assertThat(t4.getState(), not(equalTo(State.COMPLETED)));
            // After 4 seconds, t3 should have completed.
            Thread.sleep(waitTime * 2 + 100);
            assertThat(t3.getState(), equalTo(State.COMPLETED));
            assertThat((double) t3.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
            assertThat(t4.getState(), equalTo(State.NONE));
            WaitingTask[] completedTasks = { t1, t2, t3 };
            validateWaitingTaskResults(completedTasks);
            return;
        }
        fail("Should not get here");
    }

    @Test
    public void testAddTaskIntoShutdownPool_MultipleThreads() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime);
        WaitingTask t2 = new WaitingTask(waitTime);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        WaitingTask t7 = new WaitingTask(waitTime * 4);
        WaitingTask t8 = new WaitingTask(waitTime * 4);
        WaitingTask t9 = new WaitingTask(waitTime * 5);
        WaitingTask t10 = new WaitingTask(waitTime * 5);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 };
        validateTaskInitialState(tasks);
        PoolAddTaskCaller r1 = new PoolAddTaskCaller(pool, false, t1);
        PoolAddTaskCaller r2 = new PoolAddTaskCaller(pool, false, t2);
        PoolAddTaskCaller r3 = new PoolAddTaskCaller(pool, false, t3);
        PoolAddTaskCaller r4 = new PoolAddTaskCaller(pool, false, t4);
        PoolAddTaskCaller r5 = new PoolAddTaskCaller(pool, false, t5);
        PoolAddTaskCaller r6 = new PoolAddTaskCaller(pool, false, t6);
        PoolAddTaskCaller r7 = new PoolAddTaskCaller(pool, false, t7);
        PoolAddTaskCaller r8 = new PoolAddTaskCaller(pool, false, t8);
        PoolAddTaskCaller r9 = new PoolAddTaskCaller(pool, false, t9);
        PoolAddTaskCaller r10 = new PoolAddTaskCaller(pool, false, t10);
        validatePoolRunningState();
        List<Thread> testThreads1 = ThreadPoolTestHelper.getTestThreads(r1, r2, r3, r4, r5);
        for (Thread t : testThreads1) {
            t.start();
            Thread.sleep(10);
        }
        for (Thread t : testThreads1) {
            t.join();
        }
        pool.shutdown();
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertTrue(t.isAlive());
        }
        List<Thread> testThreads2 = ThreadPoolTestHelper.getTestThreads(r6, r7, r8, r9, r10);
        for (Thread t : testThreads2) {
            t.start();
            Thread.sleep(10);
        }
        for (Thread t : testThreads2) {
            t.join();
        }
        // After 0 seconds, no tasks should have completed.
        assertThat(t1.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t2.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t3.getState(), equalTo(State.NONE));
        assertThat(t4.getState(), equalTo(State.NONE));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 2 seconds, t1 and t2 should have completed.
        Thread.sleep(waitTime * 1 + 100);
        assertThat(t1.getState(), equalTo(State.COMPLETED));
        assertThat(t2.getState(), equalTo(State.COMPLETED));
        assertThat((double) t1.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 1, 100));
        assertThat(t3.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t4.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t5.getState(), equalTo(State.NONE));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 4 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime * 2 + 100);
        assertThat(t3.getState(), equalTo(State.COMPLETED));
        assertThat(t4.getState(), equalTo(State.COMPLETED));
        assertThat((double) t3.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 2, 100));
        assertThat(t5.getState(), not(equalTo(State.COMPLETED)));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 6 seconds, t5 should have completed.
        Thread.sleep(waitTime * 3 + 100);
        assertThat(t5.getState(), equalTo(State.COMPLETED));
        assertThat((double) t5.getTotalRunTime(TimeUnit.MILLISECONDS), closeTo(waitTime * 3, 100));
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 8 seconds, no more tasks should have completed.
        Thread.sleep(waitTime * 4 + 100);
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        // After 10 seconds, no more tasks should have completed.
        Thread.sleep(waitTime * 5 + 100);
        assertThat(t6.getState(), equalTo(State.NONE));
        assertThat(t7.getState(), equalTo(State.NONE));
        assertThat(t8.getState(), equalTo(State.NONE));
        assertThat(t9.getState(), equalTo(State.NONE));
        assertThat(t10.getState(), equalTo(State.NONE));
        assertThat(t6.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
        assertThat(t7.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
        assertThat(t8.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
        assertThat(t9.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
        assertThat(t10.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
        assertTrue(pool.isShutdown());
        assertTrue(pool.isTerminated());
        assertFalse(poolThreads.threads[0].isAlive());
        assertFalse(poolThreads.threads[1].isAlive());
        WaitingTask[] completedTasks = { t1, t2, t3, t4, t5 };
        validateWaitingTaskResults(completedTasks);
    }

    @Test
    public void testAwaitTermination() throws Exception
    {
        validatePoolRunningState();
        Stopwatch sw = Stopwatch.createUnstarted();

        sw.start();
        long waitTime1 = 500;
        boolean terminated1 = pool.awaitTermination(waitTime1, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds1 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(terminated1);
        validatePoolRunningState();
        assertThat((double) elapsedMilliseconds1,
                closeTo(waitTime1, ThreadPool.AWAIT_TERMINATION_INTERVAL_MILLISECONDS));

        sw.reset();
        sw.start();
        long waitTime2 = 1000;
        boolean terminated2 = pool.awaitTermination(waitTime2, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds2 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(terminated2);

        validatePoolRunningState();
        assertThat((double) elapsedMilliseconds2,
                closeTo(waitTime2, ThreadPool.AWAIT_TERMINATION_INTERVAL_MILLISECONDS));

        sw.reset();
        sw.start();
        long waitTime3 = 2500;
        boolean terminated3 = pool.awaitTermination(waitTime3, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds3 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(terminated3);
        validatePoolRunningState();
        assertThat((double) elapsedMilliseconds3,
                closeTo(waitTime3, ThreadPool.AWAIT_TERMINATION_INTERVAL_MILLISECONDS));

        sw.reset();
        pool.shutdown();
        sw.start();
        long waitTime4 = 500;
        boolean terminated4 = pool.awaitTermination(waitTime4, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds4 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertTrue(terminated4);
        assertTrue(pool.isShutdown());
        assertTrue(pool.isTerminated());
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertFalse(t.isAlive());
        }
        assertThat(elapsedMilliseconds4, lessThan(waitTime4));
    }

    @Test
    public void testAwaitTermination_MultipleThreads() throws Exception
    {
        validatePoolRunningState();
        Stopwatch sw = Stopwatch.createUnstarted();

        long waitTime1 = 500;
        PoolAwaitTerminationCaller r11 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r12 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r13 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r14 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r15 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r16 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r17 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r18 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r19 = new PoolAwaitTerminationCaller(pool, waitTime1);
        PoolAwaitTerminationCaller r110 = new PoolAwaitTerminationCaller(pool, waitTime1);

        List<Thread> testThreads1 = ThreadPoolTestHelper.getTestThreads(r11, r12, r13, r14, r15, r16, r17, r18, r19,
                r110);
        sw.start();
        for (Thread t : testThreads1) {
            t.start();
        }
        for (Thread t : testThreads1) {
            t.join();
        }
        sw.stop();
        long elapsedMilliseconds1 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(r11.isTerminated);
        assertFalse(r12.isTerminated);
        assertFalse(r13.isTerminated);
        assertFalse(r14.isTerminated);
        assertFalse(r15.isTerminated);
        assertFalse(r16.isTerminated);
        assertFalse(r17.isTerminated);
        assertFalse(r18.isTerminated);
        assertFalse(r19.isTerminated);
        assertFalse(r110.isTerminated);
        validatePoolRunningState();
        assertThat((double) elapsedMilliseconds1,
                closeTo(waitTime1, ThreadPool.AWAIT_TERMINATION_INTERVAL_MILLISECONDS));

        sw.reset();
        long waitTime2 = 1000;
        PoolAwaitTerminationCaller r21 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r22 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r23 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r24 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r25 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r26 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r27 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r28 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r29 = new PoolAwaitTerminationCaller(pool, waitTime2);
        PoolAwaitTerminationCaller r210 = new PoolAwaitTerminationCaller(pool, waitTime2);
        List<Thread> testThreads2 = ThreadPoolTestHelper.getTestThreads(r21, r22, r23, r24, r25, r26, r27, r28, r29,
                r210);
        sw.start();
        for (Thread t : testThreads2) {
            t.start();
        }
        for (Thread t : testThreads2) {
            t.join();
        }
        sw.stop();
        long elapsedMilliseconds2 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(r21.isTerminated);
        assertFalse(r22.isTerminated);
        assertFalse(r23.isTerminated);
        assertFalse(r24.isTerminated);
        assertFalse(r25.isTerminated);
        assertFalse(r26.isTerminated);
        assertFalse(r27.isTerminated);
        assertFalse(r28.isTerminated);
        assertFalse(r29.isTerminated);
        assertFalse(r210.isTerminated);
        validatePoolRunningState();
        assertThat((double) elapsedMilliseconds2,
                closeTo(waitTime2, ThreadPool.AWAIT_TERMINATION_INTERVAL_MILLISECONDS));

        sw.reset();
        long waitTime3 = 2500;
        PoolAwaitTerminationCaller r31 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r32 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r33 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r34 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r35 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r36 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r37 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r38 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r39 = new PoolAwaitTerminationCaller(pool, waitTime3);
        PoolAwaitTerminationCaller r310 = new PoolAwaitTerminationCaller(pool, waitTime3);
        List<Thread> testThreads3 = ThreadPoolTestHelper.getTestThreads(r31, r32, r33, r34, r35, r36, r37, r38, r39,
                r310);
        sw.start();
        for (Thread t : testThreads3) {
            t.start();
        }
        for (Thread t : testThreads3) {
            t.join();
        }
        sw.stop();
        long elapsedMilliseconds3 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(r31.isTerminated);
        assertFalse(r32.isTerminated);
        assertFalse(r33.isTerminated);
        assertFalse(r34.isTerminated);
        assertFalse(r35.isTerminated);
        assertFalse(r36.isTerminated);
        assertFalse(r37.isTerminated);
        assertFalse(r38.isTerminated);
        assertFalse(r39.isTerminated);
        assertFalse(r310.isTerminated);
        validatePoolRunningState();
        assertThat((double) elapsedMilliseconds3,
                closeTo(waitTime3, ThreadPool.AWAIT_TERMINATION_INTERVAL_MILLISECONDS));

        sw.reset();
        long waitTime4 = 500;
        PoolAwaitTerminationCaller r41 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r42 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r43 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r44 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r45 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r46 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r47 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r48 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r49 = new PoolAwaitTerminationCaller(pool, waitTime4);
        PoolAwaitTerminationCaller r410 = new PoolAwaitTerminationCaller(pool, waitTime4);
        List<Thread> testThreads4 = ThreadPoolTestHelper.getTestThreads(r41, r42, r43, r44, r45, r46, r47, r48, r49,
                r410);
        pool.shutdown();
        sw.start();
        for (Thread t : testThreads4) {
            t.start();
        }
        for (Thread t : testThreads4) {
            t.join();
        }
        sw.stop();
        long elapsedMilliseconds4 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertTrue(r41.isTerminated);
        assertTrue(r42.isTerminated);
        assertTrue(r43.isTerminated);
        assertTrue(r44.isTerminated);
        assertTrue(r45.isTerminated);
        assertTrue(r46.isTerminated);
        assertTrue(r47.isTerminated);
        assertTrue(r48.isTerminated);
        assertTrue(r49.isTerminated);
        assertTrue(r410.isTerminated);
        assertTrue(pool.isShutdown());
        assertTrue(pool.isTerminated());
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertFalse(t.isAlive());
        }
        assertThat(elapsedMilliseconds4, lessThan(waitTime4));
    }

    @Test
    public void testGetPoolTaskCount() throws Exception
    {
        assertThat(pool.getPoolTaskCount(), equalTo(0));
        long waitTime = 200;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4 };
        validateTaskInitialState(tasks);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        pool.addTask(t4);
        Thread.sleep(50);
        // The first two tasks may have already started - it is a race so check for both
        // valid values: 2 and 4.
        assertThat(pool.getPoolTaskCount(), anyOf(equalTo(2), equalTo(4)));
        Thread.sleep(50);
        assertThat(pool.getPoolTaskCount(), equalTo(2));
        Thread.sleep(waitTime);
        assertThat(pool.getPoolTaskCount(), equalTo(0));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testGetPoolTaskCount_MultipleThreads() throws Exception
    {
        assertThat(pool.getPoolTaskCount(), equalTo(0));
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime);
        WaitingTask t2 = new WaitingTask(waitTime);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        WaitingTask t7 = new WaitingTask(waitTime * 4);
        WaitingTask t8 = new WaitingTask(waitTime * 4);
        WaitingTask t9 = new WaitingTask(waitTime * 5);
        WaitingTask t10 = new WaitingTask(waitTime * 5);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 };
        validateTaskInitialState(tasks);
        PoolAddTaskCaller r1 = new PoolAddTaskCaller(pool, false, t1);
        PoolAddTaskCaller r2 = new PoolAddTaskCaller(pool, false, t2);
        PoolAddTaskCaller r3 = new PoolAddTaskCaller(pool, false, t3);
        PoolAddTaskCaller r4 = new PoolAddTaskCaller(pool, false, t4);
        PoolAddTaskCaller r5 = new PoolAddTaskCaller(pool, false, t5);
        PoolAddTaskCaller r6 = new PoolAddTaskCaller(pool, false, t6);
        PoolAddTaskCaller r7 = new PoolAddTaskCaller(pool, false, t7);
        PoolAddTaskCaller r8 = new PoolAddTaskCaller(pool, false, t8);
        PoolAddTaskCaller r9 = new PoolAddTaskCaller(pool, false, t9);
        PoolAddTaskCaller r10 = new PoolAddTaskCaller(pool, false, t10);
        List<Thread> testThreads1 = ThreadPoolTestHelper.getTestThreads(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        for (Thread t : testThreads1) {
            t.start();
            Thread.sleep(10);
        }
        for (Thread t : testThreads1) {
            t.join();
        }
        Thread.sleep(100);
        assertThat(pool.getPoolTaskCount(), equalTo(8));
        Thread.sleep(waitTime * 1 + 100);
        assertThat(pool.getPoolTaskCount(), equalTo(6));
        Thread.sleep(waitTime * 2 + 100);
        assertThat(pool.getPoolTaskCount(), equalTo(4));
        Thread.sleep(waitTime * 3 + 100);
        assertThat(pool.getPoolTaskCount(), equalTo(2));
        Thread.sleep(waitTime * 4 + 100);
        assertThat(pool.getPoolTaskCount(), equalTo(0));
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testShutdown() throws Exception
    {
        validatePoolRunningState();
        pool.shutdown();
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        // Give the threads time to terminate.
        Thread.sleep(500);
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertFalse(t.isAlive());
        }
        assertTrue(pool.isTerminated());
        // Should do nothing.
        pool.shutdown();
    }

    @Test
    public void testShutdown_MultipleThreads() throws Exception
    {
        // Add one real task so that the pool threads will have some work to do.
        int waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1 };
        validateTaskInitialState(tasks);
        PoolAddTaskCaller r0 = new PoolAddTaskCaller(pool, false, t1);
        PoolShutdownCaller r1 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r2 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r3 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r4 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r5 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r6 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r7 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r8 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r9 = new PoolShutdownCaller(pool);
        PoolShutdownCaller r10 = new PoolShutdownCaller(pool);
        List<Thread> testThreads = ThreadPoolTestHelper.getTestThreads(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        for (Thread t : testThreads) {
            t.start();
            Thread.sleep(10);
        }
        for (Thread t : testThreads) {
            t.join();
        }
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        // Give the threads time to terminate.
        Thread.sleep(waitTime * 2);
        TestingPoolThreads poolThreads = ThreadPoolTestHelper.getPoolThreads(pool);
        for (Thread t : poolThreads.threads) {
            assertFalse(t.isAlive());
        }
        assertTrue(pool.isTerminated());
        // Should do nothing.
        pool.shutdown();
        validateWaitingTaskResults(tasks);
    }
}
