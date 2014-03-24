package com.messaggi.util;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.messaggi.junit.MessaggiLogicTestCase;

public class TestThreadPool extends MessaggiLogicTestCase
{
    private ThreadPool pool;

    @Override
    @Before
    public void setUp() throws Exception
    {
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
    }

    private Thread getThreadPoolThreadByIndex(int index) throws Exception
    {
        Field fi = pool.getClass().getDeclaredField("taskThreads");
        fi.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Thread> threads = (List<Thread>) fi.get(pool);
        return threads.get(index);
    }

    private static List<Thread> getTestThreads(Runnable... runnables)
    {
        List<Thread> testThreads = new ArrayList<Thread>();
        for (int index = 0; index < runnables.length; index++) {
            testThreads.add(new Thread(runnables[index]));
        }
        return testThreads;
    }

    private TestingPoolThreads getPoolThreads() throws Exception
    {
        Thread t1 = getThreadPoolThreadByIndex(0);
        Thread t2 = getThreadPoolThreadByIndex(1);
        return new TestingPoolThreads(t1, t2);
    }

    private void validatePoolInitialState(TestingPoolThreads poolThreads) throws Exception
    {
        assertTrue(poolThreads.t1.isAlive());
        assertFalse(poolThreads.t1.isInterrupted());
        assertTrue(poolThreads.t2.isAlive());
        assertFalse(poolThreads.t2.isInterrupted());
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
    }

    @Test
    public void testAddTask() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        long waitTime = 2000;
        WaitingTask t1 = new WaitingTask(waitTime);
        WaitingTask t2 = new WaitingTask(waitTime);
        WaitingTask t3 = new WaitingTask(waitTime);
        WaitingTask t4 = new WaitingTask(waitTime);
        WaitingTask t5 = new WaitingTask(waitTime);
        WaitingTask t6 = new WaitingTask(waitTime);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        pool.addTask(t4);
        pool.addTask(t5);
        pool.addTask(t6);
        assertFalse(t1.isCompleted);
        assertFalse(t2.isCompleted);
        assertFalse(t3.isCompleted);
        assertFalse(t4.isCompleted);
        assertFalse(t5.isCompleted);
        assertFalse(t6.isCompleted);
        // After 2 seconds, t1 and t2 should have completed.
        Thread.sleep(waitTime + 100);
        assertTrue(t1.isCompleted);
        assertTrue(t2.isCompleted);
        assertThat((double) t1.getTotalMilliseconds(), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalMilliseconds(), closeTo(waitTime * 1, 100));
        assertFalse(t3.isCompleted);
        assertFalse(t4.isCompleted);
        assertFalse(t5.isCompleted);
        assertFalse(t6.isCompleted);
        // After 4 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime + 100);
        assertTrue(t3.isCompleted);
        assertTrue(t4.isCompleted);
        assertThat((double) t3.getTotalMilliseconds(), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalMilliseconds(), closeTo(waitTime * 2, 100));
        assertFalse(t5.isCompleted);
        assertFalse(t6.isCompleted);
        // After 6 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime + 100);
        assertTrue(t5.isCompleted);
        assertTrue(t6.isCompleted);
        assertThat((double) t5.getTotalMilliseconds(), closeTo(waitTime * 3, 100));
        assertThat((double) t6.getTotalMilliseconds(), closeTo(waitTime * 3, 100));
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
    }

    @Test
    public void testAddTask_MultipleThreads() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        long waitTime = 2000;
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
        PoolAddTaskCaller r1 = new PoolAddTaskCaller(pool, t1);
        PoolAddTaskCaller r2 = new PoolAddTaskCaller(pool, t2);
        PoolAddTaskCaller r3 = new PoolAddTaskCaller(pool, t3);
        PoolAddTaskCaller r4 = new PoolAddTaskCaller(pool, t4);
        PoolAddTaskCaller r5 = new PoolAddTaskCaller(pool, t5);
        PoolAddTaskCaller r6 = new PoolAddTaskCaller(pool, t6);
        PoolAddTaskCaller r7 = new PoolAddTaskCaller(pool, t7);
        PoolAddTaskCaller r8 = new PoolAddTaskCaller(pool, t8);
        PoolAddTaskCaller r9 = new PoolAddTaskCaller(pool, t9);
        PoolAddTaskCaller r10 = new PoolAddTaskCaller(pool, t10);
        List<Thread> testThreads1 = getTestThreads(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        for (Thread t : testThreads1) {
            t.start();
            Thread.sleep(1);
        }
        for (Thread t : testThreads1) {
            t.join();
        }
        // After 0 seconds, no tasks should have completed.
        assertFalse(t1.isCompleted);
        assertFalse(t2.isCompleted);
        assertFalse(t3.isCompleted);
        assertFalse(t4.isCompleted);
        assertFalse(t5.isCompleted);
        assertFalse(t6.isCompleted);
        assertFalse(t7.isCompleted);
        assertFalse(t8.isCompleted);
        assertFalse(t9.isCompleted);
        assertFalse(t10.isCompleted);
        // After 2 seconds, t1 and t2 should have completed.
        Thread.sleep(waitTime * 1 + 100);
        assertTrue(t1.isCompleted);
        assertTrue(t2.isCompleted);
        assertThat((double) t1.getTotalMilliseconds(), closeTo(waitTime * 1, 100));
        assertThat((double) t2.getTotalMilliseconds(), closeTo(waitTime * 1, 100));
        assertFalse(t3.isCompleted);
        assertFalse(t4.isCompleted);
        assertFalse(t5.isCompleted);
        assertFalse(t6.isCompleted);
        assertFalse(t7.isCompleted);
        assertFalse(t8.isCompleted);
        assertFalse(t9.isCompleted);
        assertFalse(t10.isCompleted);
        // After 4 seconds, t3 and t4 should have completed.
        Thread.sleep(waitTime * 2 + 100);
        assertTrue(t3.isCompleted);
        assertTrue(t4.isCompleted);
        assertThat((double) t3.getTotalMilliseconds(), closeTo(waitTime * 2, 100));
        assertThat((double) t4.getTotalMilliseconds(), closeTo(waitTime * 2, 100));
        assertFalse(t5.isCompleted);
        assertFalse(t6.isCompleted);
        assertFalse(t7.isCompleted);
        assertFalse(t8.isCompleted);
        assertFalse(t9.isCompleted);
        assertFalse(t10.isCompleted);
        // After 6 seconds, t5 and t6 should have completed.
        Thread.sleep(waitTime * 3 + 100);
        assertTrue(t5.isCompleted);
        assertTrue(t6.isCompleted);
        assertThat((double) t5.getTotalMilliseconds(), closeTo(waitTime * 3, 100));
        assertThat((double) t6.getTotalMilliseconds(), closeTo(waitTime * 3, 100));
        assertFalse(t7.isCompleted);
        assertFalse(t8.isCompleted);
        assertFalse(t9.isCompleted);
        assertFalse(t10.isCompleted);
        // After 8 seconds, t7 and t8 should have completed.
        Thread.sleep(waitTime * 4 + 100);
        assertTrue(t7.isCompleted);
        assertTrue(t8.isCompleted);
        assertThat((double) t7.getTotalMilliseconds(), closeTo(waitTime * 4, 100));
        assertThat((double) t8.getTotalMilliseconds(), closeTo(waitTime * 4, 100));
        assertFalse(t9.isCompleted);
        assertFalse(t10.isCompleted);
        // After 10 seconds, t9 and t10 should have completed.
        Thread.sleep(waitTime * 5 + 100);
        assertTrue(t9.isCompleted);
        assertTrue(t10.isCompleted);
        assertThat((double) t9.getTotalMilliseconds(), closeTo(waitTime * 5, 100));
        assertThat((double) t10.getTotalMilliseconds(), closeTo(waitTime * 5, 100));
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
    }

    @Test
    @Ignore
    public void testAddTaskIntoShutdownPool() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        long waitTime1 = 2000;
        WaitingTask t1 = new WaitingTask(waitTime1);
        WaitingTask t2 = new WaitingTask(waitTime1);
        WaitingTask t3 = new WaitingTask(waitTime1);
        WaitingTask t4 = new WaitingTask(waitTime1);
        pool.addTask(t1);
        pool.addTask(t2);
        pool.addTask(t3);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        pool.shutdown();
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        try {
            pool.addTask(t4);
            fail("Should not get here");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("Attempting to assign work to shutdown thread pool."));
            assertFalse(t1.isCompleted);
            assertFalse(t2.isCompleted);
            assertFalse(t3.isCompleted);
            assertFalse(t4.isCompleted);
            // After 2 seconds, t1 and t2 should have completed.
            Thread.sleep(waitTime1 + 100);
            assertTrue(t1.isCompleted);
            assertTrue(t2.isCompleted);
            assertFalse(t3.isCompleted);
            assertFalse(t4.isCompleted);
            // After 2 more seconds, t3 should have completed.
            Thread.sleep(waitTime1 + 100);
            assertTrue(t1.isCompleted);
            assertTrue(t2.isCompleted);
            assertTrue(t3.isCompleted);
            assertFalse(t4.isCompleted);
            assertTrue(pool.isShutdown());
            assertTrue(pool.isTerminated());
            assertFalse(poolThreads.t1.isAlive());
            assertFalse(poolThreads.t2.isAlive());
            return;
        }
        fail("Should not get here");
    }

    @Test
    @Ignore
    public void testAwaitTermination() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        Stopwatch sw = Stopwatch.createUnstarted();

        sw.start();
        long waitTime1 = 1000;
        boolean terminated1 = pool.awaitTermination(waitTime1, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds1 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(terminated1);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        assertThat((double) elapsedMilliseconds1, closeTo(waitTime1, 100));

        sw.reset();
        sw.start();
        long waitTime2 = 2000;
        boolean terminated2 = pool.awaitTermination(waitTime2, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds2 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(terminated2);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        assertThat((double) elapsedMilliseconds2, closeTo(waitTime2, 100));

        sw.reset();
        sw.start();
        long waitTime3 = 5000;
        boolean terminated3 = pool.awaitTermination(waitTime3, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds3 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(terminated3);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        assertThat((double) elapsedMilliseconds3, closeTo(waitTime3, 100));

        sw.reset();
        pool.shutdown();
        sw.start();
        long waitTime4 = 1000;
        boolean terminated4 = pool.awaitTermination(waitTime4, TimeUnit.MILLISECONDS);
        sw.stop();
        long elapsedMilliseconds4 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertTrue(terminated4);
        assertTrue(pool.isShutdown());
        assertTrue(pool.isTerminated());
        assertFalse(poolThreads.t1.isAlive());
        assertFalse(poolThreads.t2.isAlive());
        assertThat(elapsedMilliseconds4, lessThan(waitTime4));
    }

    @Test
    @Ignore
    public void testAwaitTermination_MultipleThreads() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        Stopwatch sw = Stopwatch.createUnstarted();

        long waitTime1 = 1000;
        PoolAwaitTerminationCaller runnable1 = new PoolAwaitTerminationCaller(pool, waitTime1);
        //List<Thread> testThreads1 = getTestThreads(10, runnable1);
        //sw.start();
        //for (Thread t : testThreads1) {
        //    t.start();
        //}
        //for (Thread t : testThreads1) {
        //    t.join();
        //}
        sw.stop();
        long elapsedMilliseconds1 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(runnable1.isTerminated);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        assertThat((double) elapsedMilliseconds1, closeTo(waitTime1, 100));

        sw.reset();
        long waitTime2 = 2000;
        PoolAwaitTerminationCaller runnable2 = new PoolAwaitTerminationCaller(pool, waitTime2);
        //List<Thread> testThreads2 = getTestThreads(10, runnable2);
        //sw.start();
        //for (Thread t : testThreads2) {
        //    t.start();
        //}
        //for (Thread t : testThreads2) {
        //    t.join();
        //}
        sw.stop();
        long elapsedMilliseconds2 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(runnable2.isTerminated);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        assertThat((double) elapsedMilliseconds2, closeTo(waitTime2, 100));

        sw.reset();
        long waitTime3 = 5000;
        PoolAwaitTerminationCaller runnable3 = new PoolAwaitTerminationCaller(pool, waitTime3);
        //List<Thread> testThreads3 = getTestThreads(10, runnable3);
        //sw.start();
        //for (Thread t : testThreads3) {
        //    t.start();
        //}
        //for (Thread t : testThreads3) {
        //    t.join();
        //}
        sw.stop();
        long elapsedMilliseconds3 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertFalse(runnable3.isTerminated);
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
        assertThat((double) elapsedMilliseconds3, closeTo(waitTime3, 100));

        sw.reset();
        long waitTime4 = 1000;
        PoolAwaitTerminationCaller runnable4 = new PoolAwaitTerminationCaller(pool, waitTime4);
        //List<Thread> testThreads4 = getTestThreads(10, runnable4);
        //pool.shutdown();
        //sw.start();
        //for (Thread t : testThreads4) {
        //    t.start();
        //}
        //for (Thread t : testThreads4) {
        //    t.join();
        //}
        sw.stop();
        long elapsedMilliseconds4 = sw.elapsed(TimeUnit.MILLISECONDS);
        assertTrue(runnable4.isTerminated);
        assertTrue(pool.isShutdown());
        assertTrue(pool.isTerminated());
        assertFalse(poolThreads.t1.isAlive());
        assertFalse(poolThreads.t2.isAlive());
        assertThat(elapsedMilliseconds4, lessThan(waitTime4));
    }

    @Test
    @Ignore
    public void testShutdown() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        pool.shutdown();
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        // Give the threads time to terminate.
        Thread.sleep(1000);
        assertFalse(poolThreads.t1.isAlive());
        assertFalse(poolThreads.t2.isAlive());
        assertTrue(pool.isTerminated());
        // Should do nothing.
        pool.shutdown();
    }

    @Test
    @Ignore
    public void testShutdown_MultipleThreads() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        PoolShutdownCaller runnable = new PoolShutdownCaller(pool);
        //List<Thread> testThreads = getTestThreads(10, runnable);
        //for (Thread t : testThreads) {
        //    t.start();
        //}
        assertTrue(pool.isShutdown());
        assertFalse(pool.isTerminated());
        // Give the threads time to terminate.
        Thread.sleep(1000);
        assertFalse(poolThreads.t1.isAlive());
        assertFalse(poolThreads.t2.isAlive());
        assertTrue(pool.isTerminated());
        // Should do nothing.
        pool.shutdown();
    }

    @Test
    @Ignore
    public void testForcedConsumerThreadInterrupt() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        poolThreads.t1.interrupt();
        poolThreads.t2.interrupt();
        assertTrue(poolThreads.t1.isInterrupted());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isInterrupted());
        assertTrue(poolThreads.t2.isAlive());
        // Give the threads time to be interrupted.
        Thread.sleep(1000);
    }

    @Test
    @Ignore
    public void testForcedConsumerThreadInterrupt_MultipleThreads() throws Exception
    {
        TestingPoolThreads poolThreads = getPoolThreads();
        validatePoolInitialState(poolThreads);
        PoolThreadInterrupter runnable = new PoolThreadInterrupter(poolThreads.t1, poolThreads.t2);
        //List<Thread> testThreads = getTestThreads(10, runnable);
        //for (Thread t : testThreads) {
        //    t.start();
        //}
        assertTrue(poolThreads.t1.isInterrupted());
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isInterrupted());
        assertTrue(poolThreads.t2.isAlive());
        Thread.sleep(1000);
    }

    private static class WaitingTask implements Task
    {
        private final long waitTime;

        public boolean isCompleted;

        private long totalMilliseconds;

        @Override
        public String getName()
        {
            return String.format("%s - %s", this.getClass().getSimpleName(), waitTime);
        }

        @Override
        public long getTotalMilliseconds ()
        {
            return totalMilliseconds;
        }

        WaitingTask(long waitTime)
        {
            this.waitTime = waitTime;
        }

        @Override
        public void run()
        {
            Stopwatch sw = Stopwatch.createStarted();
            try {

                Thread.sleep(waitTime);
                isCompleted = true;
            } catch (InterruptedException e) {

            }
            sw.stop();
            totalMilliseconds = sw.elapsed(TimeUnit.MILLISECONDS);
        }
    }

    private static class TestingPoolThreads
    {
        public final Thread t1;

        public final Thread t2;

        TestingPoolThreads(Thread t1, Thread t2)
        {
            this.t1 = t1;
            this.t2 = t2;
        }
    }

    static class PoolAddTaskCaller implements Runnable
    {
        private final ThreadPool pool;

        private final WaitingTask waitingTask;

        public boolean isAdded;

        public PoolAddTaskCaller(ThreadPool pool, WaitingTask waitingTask)
        {
            this.pool = pool;
            this.waitingTask = waitingTask;
        }

        @Override
        public void run()
        {
            try {
                pool.addTask(waitingTask);
                isAdded = true;
            } catch (Exception e) {
            }
        }
    }

    static class PoolAwaitTerminationCaller implements Runnable
    {
        private final ThreadPool pool;

        private final long waitTimeMilliseconds;
        
        public boolean isTerminated;

        public PoolAwaitTerminationCaller(ThreadPool pool, long waitTimeMilliseconds)
        {
            this.pool = pool;
            this.waitTimeMilliseconds = waitTimeMilliseconds;
        }

        @Override
        public void run() 
        {
            try {
                isTerminated = pool.awaitTermination(waitTimeMilliseconds, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
            }
        }
    }

    static class PoolShutdownCaller implements Runnable
    {
        private final ThreadPool pool;

        public PoolShutdownCaller(ThreadPool pool)
        {
            this.pool = pool;
        }

        @Override
        public void run()
        {
            pool.shutdown();
        }
    }

    static class PoolThreadInterrupter implements Runnable
    {
        private final Thread poolThread1;

        private final Thread poolThread2;

        public PoolThreadInterrupter(Thread poolThread1, Thread poolThread2)
        {
            this.poolThread1 = poolThread1;
            this.poolThread2 = poolThread2;
        }

        @Override
        public void run()
        {
            poolThread1.interrupt();
            poolThread2.interrupt();
        }
    }
}

