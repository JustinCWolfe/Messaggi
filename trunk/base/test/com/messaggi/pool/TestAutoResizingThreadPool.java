package com.messaggi.pool;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.pool.ThreadPoolTestHelper.PoolAddTaskCaller;
import com.messaggi.pool.ThreadPoolTestHelper.WaitingTask;
import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.Task;
import com.messaggi.pool.task.Task.State;

public class TestAutoResizingThreadPool extends ThreadPoolTestCase<AutoResizingThreadPool>
{
    private static final int THREAD_COUNT_TO_SCALE_TO = 128;

    private static final HashMap<Integer, Integer> THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP = new HashMap<>();

    private static final HashMap<Integer, Integer> THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP = new HashMap<>();
    
    @BeforeClass
    public static void oneTimeSetUp () throws Exception 
    {
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
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(2, 3); // add more than 20ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(4, 5); // add more than 40ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(8, 9); // add more than 80ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(16, 17); // add more than 160ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(32, 33); // add more than 320ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(64, 65); // add more than 640ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(128, 129); // add more than 1280ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.put(256, 257); // add more than 2560ms of work

        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(2, 1); // add less than 20ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(4, 3); // add less than 40ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(8, 5); // add less than 80ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(16, 9); // add less than 160ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(32, 15); // add less than 320ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(64, 33); // add less than 640ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(128, 65); // add less than 1280ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(256, 129); // add less than 2560ms of work
        THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP.put(512, 257); // add less than 5120ms of work
    }
    
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
        Class<?> clazz = pool.getClass();
        Field fi = clazz.getDeclaredField("resizing");
        fi.setAccessible(true);
        AtomicBoolean resizing = (AtomicBoolean) fi.get(pool);
        return resizing.get();
    }

    private static ThreadPool getDuringResizeTaskQueuingThreadPool(AutoResizingThreadPool pool) throws Exception
    {
        Class<?> clazz = pool.getClass();
        Field fi = clazz.getDeclaredField("duringResizeTaskQueuingThreadPool");
        fi.setAccessible(true);
        return (ThreadPool) fi.get(pool);
    }

    private static ScheduledExecutorService getScheduledExecutorService(AutoResizingThreadPool pool) throws Exception
    {
        Class<?> clazz = pool.getClass();
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
        assertThat(ThreadPoolTestHelper.getPoolThreads(pool).threads.length, equalTo(ThreadPool.DEFAULT_THREAD_COUNT));
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
    public void testAddTaskFromMultipleThreads() throws Exception
    {
        long waitTime = 500;
        WaitingTask t1 = new WaitingTask(waitTime * 1);
        PoolAddTaskCaller atc1 = new PoolAddTaskCaller(pool, false, t1);
        WaitingTask t2 = new WaitingTask(waitTime * 1);
        Thread thread1 = new Thread(atc1);
        PoolAddTaskCaller atc2 = new PoolAddTaskCaller(pool, false, t2);
        Thread thread2 = new Thread(atc2);
        WaitingTask t3 = new WaitingTask(waitTime * 2);
        PoolAddTaskCaller atc3 = new PoolAddTaskCaller(pool, false, t3);
        Thread thread3 = new Thread(atc3);
        WaitingTask t4 = new WaitingTask(waitTime * 2);
        PoolAddTaskCaller atc4 = new PoolAddTaskCaller(pool, false, t4);
        Thread thread4 = new Thread(atc4);
        WaitingTask t5 = new WaitingTask(waitTime * 3);
        PoolAddTaskCaller atc5 = new PoolAddTaskCaller(pool, false, t5);
        Thread thread5 = new Thread(atc5);
        WaitingTask t6 = new WaitingTask(waitTime * 3);
        PoolAddTaskCaller atc6 = new PoolAddTaskCaller(pool, false, t6);
        Thread thread6 = new Thread(atc6);
        validatePoolRunningState();
        WaitingTask[] tasks = { t1, t2, t3, t4, t5, t6 };
        validateTaskInitialState(tasks);
        thread1.start();
        thread2.start();
        Thread.sleep(2);
        thread3.start();
        thread4.start();
        Thread.sleep(2);
        thread5.start();
        thread6.start();
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
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        thread6.join();
        validatePoolRunningState();
        validateWaitingTaskResults(tasks);
    }

    @Test
    public void testAddTask_WithResize() throws Exception
    {
        List<Task<?>> tasks = new ArrayList<>();

        // Put in 2 long running tasks what will span pool inspections.  These will run first
        // and then inspection task should run immediately afterwards.
        long millisecondsToCompleteInitialWork = 0;
        long longRunningTaskMilliseconds = pool.getSecondsBetweenPoolSizeInspections() * 1000 + 50;
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
            tasks.add(new WaitingTask(pool.getSecondsBetweenPoolSizeInspections()));
            millisecondsToCompleteInitialWork += pool.getSecondsBetweenPoolSizeInspections();
        }

        validatePoolRunningState();
        validateTaskInitialState(tasks.toArray(new Task<?>[tasks.size()]));
        for (Task<?> task : tasks) {
            pool.addTask(task);
        }

        // Wait for first resize to finish before adding new tasks to the pool.
        Thread.sleep(millisecondsToCompleteInitialWork);
        
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
            int numberOfNewTasksToAdd = (maxThreadCount >= THREAD_COUNT_TO_SCALE_TO) ? THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP
                    .get(poolThreadCount) : THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.get(poolThreadCount);
            if (numberOfNewTasksToAdd != previousNumberOfNewTasksToAdd) {
                previousNumberOfNewTasksToAdd = numberOfNewTasksToAdd;
            }
            // Add new tasks which will grow and then shrink number of threads in the pool.
            for (int i = 0; i < numberOfNewTasksToAdd; i++) {
                WaitingTask task = new WaitingTask(pool.getSecondsBetweenPoolSizeInspections());
                tasks.add(task);
                pool.addTask(task);
            }
            Thread.sleep(pool.getSecondsBetweenPoolSizeInspections());
            // The max thread count got all the way to 256 and we are back down to 
            // 2 threads, so the test is complete.
            if (maxThreadCount >= THREAD_COUNT_TO_SCALE_TO && poolThreadCount == 2) {
                break;
            }
        }
        assertThat (minThreadCount, equalTo(2));
        assertThat(maxThreadCount, greaterThanOrEqualTo(THREAD_COUNT_TO_SCALE_TO));
        assertThat(ThreadPoolTestHelper.getPoolThreads(pool).threads.length, equalTo(2));
        System.out.println("total task count: " + tasks.size());
        validateWaitingTaskResults(tasks.toArray(new WaitingTask[tasks.size()]));
    }

    @Test
    public void testAddTaskFromMultipleThreads_WithResize() throws Exception
    {
        AddTaskThread t1 = new AddTaskThread(pool);
        AddTaskThread t2 = new AddTaskThread(pool);
        AddTaskThread t3 = new AddTaskThread(pool);
        AddTaskThread t4 = new AddTaskThread(pool);
        AddTaskThread t5 = new AddTaskThread(pool);
        AddTaskThread t6 = new AddTaskThread(pool);
        AddTaskThread t7 = new AddTaskThread(pool);
        AddTaskThread t8 = new AddTaskThread(pool);
        AddTaskThread t9 = new AddTaskThread(pool);
        AddTaskThread t10 = new AddTaskThread(pool);
        AddTaskThread[] threads = { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 };

        // Put in 2 long running tasks what will span pool inspections.  These will run first
        // and then inspection task should run immediately afterwards.
        long millisecondsToCompleteInitialWork = 0;
        long longRunningTaskMilliseconds = pool.getSecondsBetweenPoolSizeInspections() * 1000 + 50;
        t1.tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;
        t2.tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;

        // Put in 2 long running tasks that will run immediately after pool inspection.
        t1.tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;
        t2.tasks.add(new WaitingTask(longRunningTaskMilliseconds));
        millisecondsToCompleteInitialWork += longRunningTaskMilliseconds;

        // Add a bunch of short tasks what will bump us over the resize limit.
        for (int i = 0; i < (InspectPoolQueueSizeTask.POOL_SHOULD_GROW_COUNT + 10); i++) {
            int threadIndex = i % threads.length;
            threads[threadIndex].tasks.add(new WaitingTask(pool.getSecondsBetweenPoolSizeInspections()));
            millisecondsToCompleteInitialWork += pool.getSecondsBetweenPoolSizeInspections();
        }

        validatePoolRunningState();

        t1.start();
        t2.start();
        Thread.sleep(2);
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();

        // Wait for first resize to finish before adding new tasks to the pool.
        Thread.sleep(millisecondsToCompleteInitialWork);

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
            int numberOfNewTasksToAdd = (maxThreadCount >= THREAD_COUNT_TO_SCALE_TO) ? THREAD_COUNT_TO_TASKS_TO_ADD_TO_SHRINK_POOL_MAP
                    .get(poolThreadCount) : THREAD_COUNT_TO_TASKS_TO_ADD_TO_GROW_POOL_MAP.get(poolThreadCount);
            if (numberOfNewTasksToAdd != previousNumberOfNewTasksToAdd) {
                previousNumberOfNewTasksToAdd = numberOfNewTasksToAdd;
            }
            // Add new tasks which will grow and then shrink number of threads in the pool.
            for (int i = 0; i < numberOfNewTasksToAdd; i++) {
                int threadIndex = i % threads.length;
                WaitingTask task = new WaitingTask(pool.getSecondsBetweenPoolSizeInspections());
                threads[threadIndex].tasksToAdd.put(task);
            }
            Thread.sleep(pool.getSecondsBetweenPoolSizeInspections());
            // The max thread count got all the way to 256 and we are back down to 
            // 2 threads, so the test is complete.
            if (maxThreadCount >= THREAD_COUNT_TO_SCALE_TO && poolThreadCount == 2) {
                break;
            }
        }

        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        t4.interrupt();
        t5.interrupt();
        t6.interrupt();
        t7.interrupt();
        t8.interrupt();
        t9.interrupt();
        t10.interrupt();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        t9.join();
        t10.join();

        assertThat(minThreadCount, equalTo(2));
        assertThat(maxThreadCount, greaterThanOrEqualTo(THREAD_COUNT_TO_SCALE_TO));
        assertThat(ThreadPoolTestHelper.getPoolThreads(pool).threads.length, equalTo(2));
        int totalTaskCount = 0;
        for (AddTaskThread t : threads) {
            totalTaskCount += t.tasks.size();
            validateWaitingTaskResults(t.tasks.toArray(new WaitingTask[t.tasks.size()]));
        }
        System.out.println("total task count: " + totalTaskCount);
    }

    private static class AddTaskThread extends Thread
    {
        private final AutoResizingThreadPool pool;
        private final List<Task<?>> tasks;
        private final BlockingQueue<Task<?>> tasksToAdd;

        AddTaskThread(AutoResizingThreadPool pool)
        {
            this.pool = pool;
            this.tasks = new ArrayList<>();
            this.tasksToAdd = new LinkedBlockingQueue<> ();
        }

        @Override
        public void run()
        {
            try {
                for (;;) {
                    Task<?> task = tasksToAdd.take();
                    tasks.add(task);
                    pool.addTask(task);
                }
            } catch (InterruptedException e) {

            }
        }
    }
}