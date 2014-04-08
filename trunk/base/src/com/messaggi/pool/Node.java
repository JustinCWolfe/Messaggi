package com.messaggi.pool;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.messaggi.pool.task.Task;

class Node
{
    private static final String THREAD_NAME_FORMAT = "ThreadPool-%s";

    private final int index;

    protected final BlockingDeque<Task> taskQueue;

    protected final Thread taskThread;

    Node(int index)
    {
        this.index = index;
        taskQueue = new LinkedBlockingDeque<>();
        taskThread = new Thread(new TaskConsumer(taskQueue), String.format(THREAD_NAME_FORMAT, this.index));
        taskThread.start();
    }

    void addFirst(Task task)
    {
        taskQueue.addFirst(task);
    }

    void addLast(Task task)
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

