package com.messaggi.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DelegatingThreadPool<T extends Task>
{
    private static final EndOfStreamTask POISON = new EndOfStreamTask();

    protected static final int THREAD_COUNT = 2;

    private final AtomicInteger targetThreadCounter;

    private BlockingQueue<Task> taskQueue;

    private final Thread consumerThread;

    private final Thread[] taskThreads;

    private final AtomicBoolean shutdown;

    private final Object threadAssignmentLock;

    // This is the producer.
    public void addTask(Task task)
    {
        // Setting poison multiple times shouldn't throw.
        if (task != POISON && shutdown.get()) {
            throw new RuntimeException("Attempting to assign work to terminated thread pool.");
        }
        taskQueue.add(task);
        if (task == POISON) {
            shutdown.set(true);
        }
    }

    public void addDimension()
    {

    }

    //TODO: should create implementation that assigns to thread based on 
    //algorithm that is part of an interface.  The assign method will callback
    //to the caller which should define the assignment method.
    protected int getTaskQueueIndex()
    {
        return targetThreadCounter.getAndIncrement() % THREAD_COUNT;
    }

    private void assign(Task task)
    {
        // Need to lock here so that we assign tasks in the order in which they are received.
        synchronized (threadAssignmentLock) {
            int threadIndex = getTaskQueueIndex();
            //assignCore(task, taskQueue.get(threadIndex));
        }
    }

    private void execute()
    {
        // This should create a thread that will handle requests and start it.
        consumerThread.start();

        // Spin up the worker (pool) threads that will be the consumers
        for (int threadIndex = 0; threadIndex < THREAD_COUNT; threadIndex++) {
            //taskThreads[threadIndex] = new Thread(new TaskHandler<Task>());
        }
    }

    public boolean isShutdown()
    {
        return shutdown.get();
    }

    public boolean isTerminated()
    {
        return true;
    }

    public void shutdown()
    {
        addTask(POISON);
    }

    public DelegatingThreadPool()
    {
        targetThreadCounter = new AtomicInteger();
        taskQueue = new LinkedBlockingQueue<Task>();
        consumerThread = new Thread(new TaskConsumer(taskQueue));
        shutdown = new AtomicBoolean();
        threadAssignmentLock = new Object();
        taskThreads = new Thread[THREAD_COUNT];
        for (int threadIndex = 0; threadIndex < THREAD_COUNT; threadIndex++) {
            taskThreads[threadIndex] = new Thread();
        }
        execute();
    }

    private static class EndOfStreamTask implements Task
    {
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
            for (;;) {
                try {
                    taskQueue.take();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private static class TaskHandler<T extends Task> implements Runnable
    {
        private final Task task;

        private TaskHandler(T task)
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

