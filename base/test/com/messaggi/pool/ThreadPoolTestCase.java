package com.messaggi.pool;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.messaggi.junit.MessaggiLogicTestCase;
import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.Task;
import com.messaggi.pool.task.Task.State;
import com.messaggi.pool.task.TaskBase;

public class ThreadPoolTestCase<T extends ThreadPool> extends MessaggiLogicTestCase
{
    private static final int REFLECTION_SUPERCLASS_DEPTH = 5;

    protected T pool;

    protected void validatePoolRunningState() throws Exception
    {
        assertFalse(pool.isShutdown());
        assertFalse(pool.isTerminated());
        TestingPoolThreads poolThreads = getPoolThreads(pool);
        assertTrue(poolThreads.t1.isAlive());
        assertTrue(poolThreads.t2.isAlive());
    }

    protected void validateTaskInitialState(Task<?>... tasks) throws Exception
    {
        for (Task<?> task : tasks) {
            assertThat(task.getTotalRunTime(TimeUnit.MILLISECONDS), equalTo(0L));
            assertThat(task.getState(), equalTo(State.NONE));
        }
    }

    private static Thread getThreadPoolThreadByIndex(ThreadPool pool, int index) throws Exception
    {
        Class<?> clazz = pool.getClass();
        Field fi = null;
        for (int depth = 0; depth < REFLECTION_SUPERCLASS_DEPTH; depth++) {
            try {
                fi = clazz.getDeclaredField("nodes");
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        fi.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Node> nodes = (List<Node>) fi.get(pool);
        List<Thread> threads = new ArrayList<>();
        for (Node node : nodes) {
            threads.add(node.getTaskThread());
        }
        return threads.get(index);
    }

    protected static List<Thread> getTestThreads(Runnable... runnables)
    {
        List<Thread> testThreads = new ArrayList<Thread>();
        for (int index = 0; index < runnables.length; index++) {
            testThreads.add(new Thread(runnables[index]));
        }
        return testThreads;
    }

    protected TestingPoolThreads getPoolThreads(ThreadPool pool) throws Exception
    {
        Thread t1 = getThreadPoolThreadByIndex(pool, 0);
        Thread t2 = getThreadPoolThreadByIndex(pool, 1);
        return new TestingPoolThreads(t1, t2);
    }

    protected static class TestingPoolThreads
    {
        public final Thread t1;

        public final Thread t2;

        protected TestingPoolThreads(Thread t1, Thread t2)
        {
            this.t1 = t1;
            this.t2 = t2;
        }
    }

    protected static class WaitingTask extends TaskBase<Void>
    {
        private final long waitTime;

        @Override
        public String getName()
        {
            return String.format("%s - %s", this.getClass().getSimpleName(), waitTime);
        }

        public WaitingTask(long waitTime)
        {
            super();
            this.waitTime = waitTime;
        }

        @Override
        protected void runInternal()
        {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                // Reset interrupt flag.
                Thread.currentThread().interrupt();
            }
        }
    }

    protected static class PoolAddTaskCaller implements Runnable
    {
        private final ThreadPool pool;

        private final WaitingTask waitingTask;

        private final boolean addToFront;

        public boolean isAdded;

        public PoolAddTaskCaller(ThreadPool pool, WaitingTask waitingTask, boolean addToFront)
        {
            this.addToFront = addToFront;
            this.pool = pool;
            this.waitingTask = waitingTask;
        }

        @Override
        public void run()
        {
            try {
                pool.addTaskInternal(waitingTask, addToFront);
                isAdded = true;
            } catch (Exception e) {
            }
        }
    }

    protected static class PoolAwaitTerminationCaller implements Runnable
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

    protected static class PoolShutdownCaller implements Runnable
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

    protected static class PoolInspector implements Runnable
    {
        public final InspectPoolQueueSizeTask task;

        public PoolInspector(InspectPoolQueueSizeTask task)
        {
            this.task = task;
        }

        @Override
        public void run()
        {
            task.run();
        }
    }

    public static class MockReceiveResultThreadPool extends AutoResizingThreadPool
    {
    }
}
