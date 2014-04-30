package com.messaggi.pool;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.Task;
import com.messaggi.pool.task.Task.State;

public class TestAutoResizingThreadPool extends ThreadPoolTestCase<AutoResizingThreadPool>
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        pool = new AutoResizingThreadPool(1, 2);
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
        assertThat(getPoolThreads(pool).threads.length, equalTo(ThreadPool.DEFAULT_THREAD_COUNT));
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
    public void testAddTask_WithResize() throws Exception
    {
        long waitTime = 10;
        List<Task<?>> tasks = new ArrayList<>();

        // Put in 2 long running tasks what will span pool inspections.  These will run first
        // and then inspection task should run immediately afterwards.
        long millisecondsToCompleteInitialWork = 0;
        long longRunningTaskMilliseconds = pool.getSecondsBetweenPoolSizeInspections() * 1100;
        tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;
        tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;

        // Put in 2 long running tasks that will run immediately after pool inspection.
        tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;
        tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;

        // Add a bunch of short tasks what will bump us over the resize limit.
        for (int i = 0; i < (InspectPoolQueueSizeTask.POOL_SHOULD_GROW_COUNT + 10); i++) {
            tasks.add(new WaitingTask(waitTime));
            millisecondsToCompleteInitialWork += waitTime;
        }

        validatePoolRunningState();
        validateTaskInitialState(tasks.toArray(new Task<?>[tasks.size()]));
        for (Task<?> task : tasks) {
            pool.addTask(task);
        }

        // Wait for first resize to finish before adding new tasks to the pool.
        Thread.sleep(millisecondsToCompleteInitialWork);
        
        // Computation available based on thread count (loop executes every 10ms).
        // 2 threads * 10ms = 20ms of computation
        // 4 threads * 10ms = 40ms of computation
        // 8 threads * 10ms = 80ms of computation
        // 16 threads * 10ms = 160ms of computation
        // 32 threads * 10ms = 320ms of computation
        // 64 threads * 10ms = 640ms of computation
        // 128 threads * 10ms = 1280ms of computation
        // 256 threads * 10ms = 2560ms of computation
        // 512 threads * 10ms = 5120ms of computation

        // Tasks added during each loop iteration.
        // 1 tasks * 10ms wait time = 10ms (accomplished by 2 threads)
        // 3 tasks * 10ms wait time = 30ms (accomplished by 4 threads)
        // 5 tasks * 10ms wait time = 50ms (accomplished by 8 threads)
        // 10 tasks * 10ms wait time = 100ms (accomplished by 16 threads)
        // 25 tasks * 10ms wait time = 250ms (accomplished by 32 threads)
        // 40 tasks * 10ms wait time = 400ms (accomplished by 64 threads)
        // 100 tasks * 10ms wait time = 1000ms (accomplished by 128 threads)
        // 150 tasks * 10ms wait time = 1500ms (accomplished by 256 threads)
        // 300 tasks * 10ms wait time = 3000ms (accomplished by 512 threads)

        HashMap<Integer, Integer> threadCountToTasksToAddToGrowPoolMap = new HashMap<>();
        threadCountToTasksToAddToGrowPoolMap.put(2, 3); // add more than 20ms of work
        threadCountToTasksToAddToGrowPoolMap.put(4, 5); // add more than 40ms of work
        threadCountToTasksToAddToGrowPoolMap.put(8, 9); // add more than 80ms of work
        threadCountToTasksToAddToGrowPoolMap.put(16, 17); // add more than 160ms of work
        threadCountToTasksToAddToGrowPoolMap.put(32, 33); // add more than 320ms of work
        threadCountToTasksToAddToGrowPoolMap.put(64, 65); // add more than 640ms of work
        threadCountToTasksToAddToGrowPoolMap.put(128, 129); // add more than 1280ms of work
        
        HashMap<Integer, Integer> threadCountToTasksToAddToShrinkPoolMap = new HashMap<>();
        threadCountToTasksToAddToShrinkPoolMap.put(2, 1); // add less than 20ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(4, 3); // add less than 40ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(8, 5); // add less than 80ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(16, 9); // add less than 160ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(32, 15); // add less than 320ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(64, 33); // add less than 640ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(128, 65); // add less than 1280ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(256, 129); // add less than 2560ms of work
        threadCountToTasksToAddToShrinkPoolMap.put(512, 257); // add less than 5120ms of work
        
        int threadCountToScaleTo = 128;
        int maxThreadCount = Integer.MIN_VALUE;
        int minThreadCount = Integer.MAX_VALUE;
        // Once we detect (via inspection) that the pool needs to be resized, we should run 
        // down existing tasks and then resize. Throughout this process we will keep adding 
        // tasks which should get added to a temp thread pool and then get run as the temp 
        // thread pool nodes get activated (become the main thread pool nodes).  As tasks 
        // get run down, the pool should shrink back to its default size.
        int previousNumberOfNewTasksToAdd = 0;
        for (;;) {
            int poolThreadCount = pool.threadCount;
            if (poolThreadCount > maxThreadCount) {
                maxThreadCount = poolThreadCount;
            }
            if (poolThreadCount < minThreadCount) {
                minThreadCount = poolThreadCount;
            }
            // Once we have scaled to 128 threads, dial back the number of new tasks to be
            // added on each loop iteration so that pool will shrink back down to 2 threads.
            int numberOfNewTasksToAdd = (maxThreadCount >= threadCountToScaleTo) ? threadCountToTasksToAddToShrinkPoolMap
                    .get(poolThreadCount) : threadCountToTasksToAddToGrowPoolMap.get(poolThreadCount);
            if (numberOfNewTasksToAdd != previousNumberOfNewTasksToAdd) {
                System.out.println("\t\tAdding: " + numberOfNewTasksToAdd);
                previousNumberOfNewTasksToAdd = numberOfNewTasksToAdd;
            }
            // Add new tasks which will grow and then shrink number of threads in the pool.
            for (int i = 0; i < numberOfNewTasksToAdd; i++) {
                WaitingTask task = new WaitingTask(waitTime);
                tasks.add(task);
                pool.addTask(task);
            }
            Thread.sleep(waitTime);
            // The max thread count got all the way to 256 and we are back down to 
            // 2 threads, so the test is complete.
            if (maxThreadCount >= threadCountToScaleTo && poolThreadCount == 2) {
                break;
            }
        }
        System.out.println("max thread count: " + maxThreadCount);
        System.out.println("min thread count: " + minThreadCount);
        System.out.println("total task count: " + tasks.size());
        
        assertThat (minThreadCount, equalTo(2));
        assertThat(maxThreadCount, equalTo(16));
        assertThat(getPoolThreads(pool).threads.length, equalTo(2));

        // Wait for all
        for (Task<?> task : tasks) {
            //inspectPoolQueueSizeTask.getResult()
        }
    }
}