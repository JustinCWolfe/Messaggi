package com.messaggi.pool;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.messaggi.pool.task.Task;

class Node
{
    private static final String DEFAULT_THREAD_NAME_PREFIX = "Node";

    private static final String THREAD_NAME_FORMAT = "%s-%s";

    private final int index;

    protected final BlockingDeque<Task<?>> taskQueue;

    protected final Thread taskThread;

    private final String threadNamePrefix;

    Node(int index)
    {
        this(index, DEFAULT_THREAD_NAME_PREFIX);
    }

    Node(int index, String threadNamePrefix)
    {
        this.index = index;
        this.threadNamePrefix = threadNamePrefix;
        taskQueue = new LinkedBlockingDeque<>();
        taskThread = new Thread(new TaskConsumer(taskQueue), String.format(THREAD_NAME_FORMAT, this.threadNamePrefix,
                this.index));
    }

    void startTaskThread()
    {
        taskThread.start();
    }

    void addFirst(Task<?> task)
    {
        taskQueue.addFirst(task);
    }

    void addLast(Task<?> task)
    {
        taskQueue.addLast(task);
    }

    Thread getTaskThread()
    {
        return taskThread;
    }

    int size()
    {
        return taskQueue.size();
    }
}

