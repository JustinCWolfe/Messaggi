package com.messaggi.pool;

import java.util.concurrent.BlockingQueue;

import com.messaggi.pool.task.EndOfStreamTask;
import com.messaggi.pool.task.Task;

public class TaskConsumer implements Runnable
{
    private boolean interrupted;

    private final BlockingQueue<Task<?>> taskQueue;

    TaskConsumer(BlockingQueue<Task<?>> taskQueue)
    {
        this.taskQueue = taskQueue;
    }

    public boolean isInterrupted()
    {
        return interrupted;
    }

    @Override
    public void run()
    {
        interrupted = false;
        String threadName = Thread.currentThread().getName();
        System.out.println("Starting..." + threadName);
        for (;;) {
            try {
                //System.out.println("Waiting for work..." + threadName);
                Task<?> task = taskQueue.take();
                if (EndOfStreamTask.POISON == task) {
                    if (interrupted) {
                        // Reset interrupt flag.
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Drank poison. Terminating..." + threadName);
                    return;
                }
                //System.out.println("Doing work on..." + threadName + "(" + task.getName() + ")");
                task.run();
            } catch (InterruptedException e) {
                System.out.println("Interrupted..." + threadName);
                interrupted = true;
            }
        }
    }
}

