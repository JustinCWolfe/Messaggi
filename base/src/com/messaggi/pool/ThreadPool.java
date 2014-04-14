package com.messaggi.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.messaggi.pool.task.EndOfStreamTask;
import com.messaggi.pool.task.Task;

public class ThreadPool
{
    // This is accessed by unit tests so must be package protected scoped.
    static final long AWAIT_TERMINATION_INTERVAL_MILLISECONDS = 200;

    // This is accessed by unit tests so must be package protected scoped.
    static final int DEFAULT_THREAD_COUNT = 2;

    protected List<Node> nodes;

    private AtomicBoolean shutdown;

    private AtomicInteger targetThreadCounter;

    private Object threadAssignmentLock;

    protected int threadCount;

    public ThreadPool()
    {
        this(DEFAULT_THREAD_COUNT);
    }
    
    public ThreadPool(int threadCount)
    {
        this(threadCount, true);
    }

    public ThreadPool(int threadCount, boolean startNodeTaskThreads)
    {
        initialize(createNodes(threadCount), startNodeTaskThreads);
    }

    private void addTaskToBack(Task<?> task, int index)
    {
        nodes.get(index).addLast(task);
    }

    private void addTaskToFront(Task<?> task, int index)
    {
        nodes.get(index).addFirst(task);
    }

    protected void addTaskInternal(Task<?> task, boolean addToFront)
    {
        synchronized (threadAssignmentLock) {
            if (shutdown.get()) {
                throw new RuntimeException("Attempting to assign work to shutdown thread pool.");
            }
            int index = getTaskNodeIndex();
            if (addToFront) {
                addTaskToFront(task, index);
            } else {
                addTaskToBack(task, index);
            }
        }
    }

    public void addTask(Task<?> task)
    {
        addTaskInternal(task, false);
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

    // Final because this is called from the constructor.
    protected final List<Node> createNodes(int threadCount)
    {
        List<Node> nodes = new ArrayList<>(threadCount);
        for (int index = 0; index < threadCount; index++) {
            nodes.add(new Node(index));
        }
        return nodes;
    }

    public int getPoolTaskCount()
    {
        int taskCount = 0;
        synchronized (threadAssignmentLock) {
            for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
                taskCount += nodes.get(threadIndex).size();
            }
        }
        return taskCount;
    }

    protected int getTaskNodeIndex()
    {
        return targetThreadCounter.getAndIncrement() % threadCount;
    }

    // Final because this is called from the constructor.
    protected final void initialize(List<Node> nodes, boolean startNodeTaskThreads)
    {
        this.nodes = nodes;
        this.shutdown = new AtomicBoolean();
        this.targetThreadCounter = new AtomicInteger();
        this.threadAssignmentLock = new Object();
        this.threadCount = nodes.size();
        if (startNodeTaskThreads) {
            startNodeTaskThreads();
        }
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
            for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
                anyThreadAlive |= nodes.get(threadIndex).getTaskThread().isAlive();
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
            for (int index = 0; index < threadCount; index++) {
                addTaskToBack(EndOfStreamTask.POISON, index);
            }
        }
    }

    private void startNodeTaskThreads()
    {
        for (int index = 0; index < threadCount; index++) {
            nodes.get(index).startTaskThread();
        }
    }
}

