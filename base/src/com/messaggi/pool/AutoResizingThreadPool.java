package com.messaggi.pool;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.InspectPoolQueueSizeTask.PoolSizeOpinion;
import com.messaggi.pool.task.Task;

public class AutoResizingThreadPool extends ThreadPool
{
    private static final int GROW_SHRINK_FACTOR = 2;

    private static final long SECONDS_BETWEEN_POOL_AWAIT_TERMINATION_CALLS = 10;

    private static final long SECONDS_BETWEEN_POOL_SIZE_INSPECTION = 20;

    private static final int SCHEDULED_SERVICE_POOL_SIZE = 2;

    private final AtomicBoolean resizing;

    private final BlockingDeque<Task<?>> resizingTemporaryTaskQueue;

    private final ScheduledExecutorService scheduledExecutorService;

    public AutoResizingThreadPool()
    {
        super();
        resizing = new AtomicBoolean();
        resizingTemporaryTaskQueue = new LinkedBlockingDeque<>();
        scheduledExecutorService = Executors.newScheduledThreadPool(SCHEDULED_SERVICE_POOL_SIZE);
        scheduledExecutorService.schedule(new ScheduledInspectPoolQueueSizeTask(this),
                SECONDS_BETWEEN_POOL_SIZE_INSPECTION, TimeUnit.SECONDS);
    }

    @Override
    protected void addTaskInternal(Task<?> task, boolean addToFront)
    {
        if (isResizing()) {
            if (addToFront) {
                resizingTemporaryTaskQueue.addFirst(task);
            } else {
                resizingTemporaryTaskQueue.addLast(task);
            }
        } else {
            super.addTaskInternal(task, addToFront);
        }
    }

    private boolean isResizing()
    {
        return resizing.get();
    }

    private void processInspectionResult(PoolSizeOpinion result)
    {
        Integer newThreadCount = null;
        if (result == PoolSizeOpinion.SHOULD_GROW) {
            newThreadCount = threadCount * GROW_SHRINK_FACTOR;
        } else if (result == PoolSizeOpinion.SHOULD_SHRINK) {
            if (threadCount > DEFAULT_THREAD_COUNT) {
                newThreadCount = threadCount / GROW_SHRINK_FACTOR;
            }
        }
        if (newThreadCount != null) {
            resize(newThreadCount);
        }
    }

    private void resize(int newThreadCount)
    {
        resizing.set(true);
        if (resizingTemporaryTaskQueue.size() > 0) {
            throw new RuntimeException("Tasks remaining on resizing queue: " + resizingTemporaryTaskQueue.size());
        }
        // Do not shut down the scheduled executor service - just poison the pool threads (so we
        // call super.shutdown instead of shutdown).
        super.shutdown();
        for (;;) {
            System.out.println("Tasks remaining: " + getPoolTaskCount());
            try {
                if (awaitTermination(SECONDS_BETWEEN_POOL_AWAIT_TERMINATION_CALLS, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {

            }
        }
        initialize(newThreadCount);
        //TODO: Transfer temporary tasks to new pool task queues.
        resizing.set(false);
    }

    @Override
    public void shutdown()
    {
        scheduledExecutorService.shutdown();
        super.shutdown();
    }

    private static class ScheduledInspectPoolQueueSizeTask implements Runnable
    {
        private final InspectPoolQueueSizeTask inspectPoolQueueSizeTask;

        private final AutoResizingThreadPool pool;

        private ScheduledInspectPoolQueueSizeTask(AutoResizingThreadPool pool)
        {
            this.inspectPoolQueueSizeTask = new InspectPoolQueueSizeTask(pool);
            this.pool = pool;
        }

        @Override
        public void run()
        {
            if (!pool.isResizing()) {
                pool.addTaskInternal(inspectPoolQueueSizeTask, true);
                pool.processInspectionResult(inspectPoolQueueSizeTask.getResult());
            }
        }
    }
}

