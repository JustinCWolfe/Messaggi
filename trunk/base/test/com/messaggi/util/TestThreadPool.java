package com.messaggi.util;

// import static org.hamcrest.Matchers.t
// import static org.hamcrest.Matchers.no
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestThreadPool
{
    private ThreadPool pool;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
        pool = new ThreadPool();
    }

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

    private static List<Thread> getTestThreads(int numberOfThreads, Runnable runnable)
    {
        List<Thread> testThreads = new ArrayList<Thread>();
        for (int index = 0; index < numberOfThreads; index++) {
            testThreads.add(new Thread(runnable));
        }
        return testThreads;
    }

    @Test
    @Ignore
    public void testAddTask() throws Exception
    {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testAwaitTermination() throws Exception
    {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testIsShutdown() throws Exception
    {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testIsTerminated() throws Exception
    {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testShutdown() throws Exception
    {
        Thread t1 = getThreadPoolThreadByIndex(0);
        Thread t2 = getThreadPoolThreadByIndex(1);
        assertTrue(t1.isAlive());
        assertTrue(t2.isAlive());
        assertFalse(pool.isShutdown());
        pool.shutdown();
        assertTrue(pool.isShutdown());
        // Give the threads time to terminate.
        Thread.sleep(1000);
        assertFalse(t1.isAlive());
        assertFalse(t2.isAlive());
        // Should do nothing.
        pool.shutdown();
    }

    @Test
    @Ignore
    public void testShutdown_MultipleThreads() throws Exception
    {
        Thread t1 = getThreadPoolThreadByIndex(0);
        Thread t2 = getThreadPoolThreadByIndex(1);
        assertTrue(t1.isAlive());
        assertTrue(t2.isAlive());
        assertFalse(pool.isShutdown());
        PoolShutdownCaller runnable = new PoolShutdownCaller(pool);
        List<Thread> testThreads = getTestThreads(10, runnable);
        for (Thread t : testThreads) {
            t.start();
        }
        assertTrue(pool.isShutdown());
        // Give the threads time to terminate.
        Thread.sleep(1000);
        assertFalse(t1.isAlive());
        assertFalse(t2.isAlive());
        // Should do nothing.
        pool.shutdown();
    }

    @Test
    @Ignore
    public void testForcedConsumerThreadInterrupt() throws Exception
    {
        Thread t1 = getThreadPoolThreadByIndex(0);
        Thread t2 = getThreadPoolThreadByIndex(1);
        assertTrue(t1.isAlive());
        assertFalse(t1.isInterrupted());
        assertTrue(t2.isAlive());
        assertFalse(t2.isInterrupted());
        t1.interrupt();
        t2.interrupt();
        assertTrue(t1.isInterrupted());
        assertTrue(t1.isAlive());
        assertTrue(t2.isInterrupted());
        assertTrue(t2.isAlive());
        // Give the threads time to be interrupted.
        Thread.sleep(1000);
    }

    @Test
    public void testForcedConsumerThreadInterrupt_MultipleThreads() throws Exception
    {
        Thread t1 = getThreadPoolThreadByIndex(0);
        Thread t2 = getThreadPoolThreadByIndex(1);
        assertTrue(t1.isAlive());
        assertFalse(t1.isInterrupted());
        assertTrue(t2.isAlive());
        assertFalse(t2.isInterrupted());
        PoolThreadInterrupter runnable = new PoolThreadInterrupter(t1, t2);
        List<Thread> testThreads = getTestThreads(10, runnable);
        for (Thread t : testThreads) {
            t.start();
        }
        assertTrue(t1.isInterrupted());
        assertTrue(t1.isAlive());
        assertTrue(t2.isInterrupted());
        assertTrue(t2.isAlive());
        Thread.sleep(1000);
    }

    @Test
    @Ignore
    public void testAddTaskIntoShutdownPool() throws Exception
    {
        fail("Not yet implemented");
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

