package com.messaggi.pool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.TaskBase;

public class ThreadPoolTestHelper
{
    private static final int REFLECTION_SUPERCLASS_DEPTH = 5;

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

    public static List<Thread> getTestThreads(Runnable... runnables)
    {
        List<Thread> testThreads = new ArrayList<Thread>();
        for (int index = 0; index < runnables.length; index++) {
            testThreads.add(new Thread(runnables[index]));
        }
        return testThreads;
    }

    public static TestingPoolThreads getPoolThreads(ThreadPool pool) throws Exception
    {
        Thread[] threads = new Thread[pool.threadCount];
        for (int index = 0; index < pool.threadCount; index++) {
            threads[index] = getThreadPoolThreadByIndex(pool, index);
        }
        return new TestingPoolThreads(threads);
    }

    public static class TestingPoolThreads
    {
        public final Thread[] threads;

        protected TestingPoolThreads(Thread... threads)
        {
            this.threads = threads;
        }
    }

    public static class WaitingTask extends TaskBase<Boolean>
    {
        private final long waitTime;

        public WaitingTask(long waitTime)
        {
            super();
            this.waitTime = waitTime;
        }

        @Override
        public String getName()
        {
            return String.format("%s - %s", this.getClass().getSimpleName(), waitTime);
        }

        @Override
        protected Boolean getTaskResult()
        {
            // Return true signifying that the task was run.
            return true;
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

    public static class PoolAddTaskCaller implements Runnable
    {
        private final ThreadPool pool;

        private final WaitingTask[] waitingTasks;

        private final boolean addToFront;

        public boolean isAdded;

        public PoolAddTaskCaller(ThreadPool pool, boolean addToFront, WaitingTask... waitingTasks)
        {
            this.addToFront = addToFront;
            this.pool = pool;
            this.waitingTasks = waitingTasks;
        }

        @Override
        public void run()
        {
            try {
                for (WaitingTask waitingTask : waitingTasks) {
                    pool.addTaskInternal(waitingTask, addToFront);
                }
                isAdded = true;
            } catch (Exception e) {
            }
        }
    }

    public static class PoolAwaitTerminationCaller implements Runnable
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

    public static class PoolShutdownCaller implements Runnable
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

    public static class PoolInspector implements Runnable
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
}
