package com.messaggi.pool;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.pool.ThreadPoolTestCase.MockAutoResizingThreadPool;
import com.messaggi.pool.task.Task.State;

public class TestAutoResizingThreadPool extends ThreadPoolTestCase<MockAutoResizingThreadPool>
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        pool = new MockAutoResizingThreadPool();
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

    private static boolean getIsResizing(AutoResizingThreadPool pool) throws Exception
    {
        Class<?> clazz = pool.getClass().getSuperclass();
        Field fi = clazz.getDeclaredField("resizing");
        fi.setAccessible(true);
        AtomicBoolean resizing = (AtomicBoolean) fi.get(pool);
        return resizing.get();
    }

    private static ThreadPool getDuringResizeTaskQueuingThreadPool(AutoResizingThreadPool pool) throws Exception
    {
        Class<?> clazz = pool.getClass().getSuperclass();
        Field fi = clazz.getDeclaredField("duringResizeTaskQueuingThreadPool");
        fi.setAccessible(true);
        return (ThreadPool) fi.get(pool);
    }

    private static ScheduledExecutorService getScheduledExecutorService(AutoResizingThreadPool pool) throws Exception
    {
        Class<?> clazz = pool.getClass().getSuperclass();
        Field fi = clazz.getDeclaredField("scheduledExecutorService");
        fi.setAccessible(true);
        return (ScheduledExecutorService) fi.get(pool);
    }

    @Override
    protected void validatePoolRunningState() throws Exception
    {
        super.validatePoolRunningState();
        assertFalse(getIsResizing(pool));
        assertThat(getDuringResizeTaskQueuingThreadPool(pool), nullValue());
        assertFalse(getScheduledExecutorService(pool).isShutdown());
        assertFalse(getScheduledExecutorService(pool).isTerminated());
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
        validateTaskInitialState(t1, t2, t3, t4, t5, t6);
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
    }

    @Test
    public void testAddTask2() throws Exception
    {

    }
}