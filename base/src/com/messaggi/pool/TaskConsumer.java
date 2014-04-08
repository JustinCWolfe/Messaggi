package com.messaggi.pool;

import java.util.concurrent.BlockingQueue;

import com.messaggi.pool.task.EndOfStreamTask;
import com.messaggi.pool.task.Task;

public class TaskConsumer implements Runnable
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
                if (EndOfStreamTask.POISON == task) {
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

