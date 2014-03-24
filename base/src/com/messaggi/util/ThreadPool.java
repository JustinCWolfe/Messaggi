package com.messaggi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: should implement ExecutorService.
public class ThreadPool
{
    private static final String THREAD_NAME_FORMAT = "ThreadPool-%s";

    private static final long AWAIT_TERMINATION_INTERVAL_MILLISECONDS = 200;

    private static final EndOfStreamTask POISON = new EndOfStreamTask();

    private static final int THREAD_COUNT = 2;

    private final AtomicBoolean shutdown;

    private final AtomicInteger targetThreadCounter;

    private final List<BlockingQueue<Task>> taskQueues;

    private final List<Thread> taskThreads;

    private final Object threadAssignmentLock;

    private void addTaskCore(Task task, int index)
    {
        taskQueues.get(index).add(task);
    }

    // This is the producer.
    public void addTask(Task task)
    {
        synchronized (threadAssignmentLock) {
            if (shutdown.get()) {
                throw new RuntimeException("Attempting to assign work to shutdown thread pool.");
            }
            int index = getTaskQueueIndex();
            addTaskCore(task, index);
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
    {
        long currentWaitTimeMilliseconds = 0;
        long timeoutMilliseconds = unit.toMillis(timeout);
        for (;;) {
            if (isTerminated()) {
                return true;
            }
            if (currentWaitTimeMilliseconds >= timeoutMilliseconds) {
                return false;
            }
            currentWaitTimeMilliseconds += AWAIT_TERMINATION_INTERVAL_MILLISECONDS;
            Thread.sleep(AWAIT_TERMINATION_INTERVAL_MILLISECONDS);
        }
    }

    protected int getTaskQueueIndex()
    {
        return targetThreadCounter.getAndIncrement() % THREAD_COUNT;
    }

    public boolean isShutdown()
    {
        return shutdown.get();
    }

    public boolean isTerminated()
    {
        // Cannot be terminated unless the executor service has been shutdown.
        if (isShutdown()) {
            // All threads in the pool must be terminated for this to be true.
            boolean anyThreadAlive = false;
            for (int threadIndex = 0; threadIndex < THREAD_COUNT; threadIndex++) {
                anyThreadAlive |= taskThreads.get(threadIndex).isAlive();
            }
            return !anyThreadAlive;
        }
        return false;
    }

    public void shutdown()
    {
        synchronized (threadAssignmentLock) {
            boolean alive = shutdown.compareAndSet(false, true);
            if (!alive) {
                return;
            }
            for (int index = 0; index < THREAD_COUNT; index++) {
                addTaskCore(POISON, index);
            }
        }
    }

    public ThreadPool()
    {
        shutdown = new AtomicBoolean();
        targetThreadCounter = new AtomicInteger();
        threadAssignmentLock = new Object();
        taskQueues = new ArrayList<BlockingQueue<Task>>(THREAD_COUNT);
        taskThreads = new ArrayList<Thread>(THREAD_COUNT);
        for (int index = 0; index < THREAD_COUNT; index++) {
            BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
            taskQueues.add(queue);
            Thread thread = new Thread(new TaskConsumer(queue), String.format(THREAD_NAME_FORMAT, index));
            taskThreads.add(thread);
            thread.start();
        }
    }

    private static class EndOfStreamTask implements Task
    {
        private static final String NAME = "EndOfStreamTask";

        @Override
        public String getName()
        {
            return NAME;
        }

        /**
         * This task doesn't actually run. It is used to signal the thread pool threads to terminate.
         */
        @Override
        public long getTotalMilliseconds()
        {
            return 0;
        }

        @Override
        public void run()
        {
            throw new RuntimeException("Threads should die now");
        }
    }

    private static class TaskConsumer implements Runnable
    {
        private final BlockingQueue<Task> taskQueue;

        TaskConsumer(BlockingQueue<Task> taskQueue)
        {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run()
        {
            String threadName = Thread.currentThread().getName();
            System.out.println("Starting..." + threadName);
            for (;;) {
                try {
                    System.out.println("Waiting for work..." + threadName);
                    Task task = taskQueue.take();
                    if (POISON == task) {
                        System.out.println("Drank poison. Terminating..." + threadName);
                        return;
                    }
                    System.out.println("Doing work on..." + threadName + "(" + task.getName() + ")");
                    task.run();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted..." + threadName);
                }
            }
        }
    }
}

