package com.messaggi.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.messaggi.pool.task.InspectPoolQueueSizeTask;
import com.messaggi.pool.task.InspectPoolQueueSizeTask.PoolSizeOpinion;
import com.messaggi.pool.task.TaskBase.ReceiveResult;

public class AutoResizingThreadPool extends ThreadPool implements ReceiveResult<PoolSizeOpinion>
{
    private static final long SECONDS_BETWEEN_POOL_SIZE_INSPECTION = 5;

    private static final int SCHEDULED_SERVICE_POOL_SIZE = 2;

    private final ScheduledExecutorService scheduledExecutorService;

    public AutoResizingThreadPool()
    {
        super();
        scheduledExecutorService = Executors.newScheduledThreadPool(SCHEDULED_SERVICE_POOL_SIZE);
        scheduledExecutorService.schedule(new ScheduledInspectPoolQueueSizeTask(this),
                SECONDS_BETWEEN_POOL_SIZE_INSPECTION, TimeUnit.SECONDS);
    }

    @Override
    public void receiveTaskResult(PoolSizeOpinion result)
    {
        if (result == PoolSizeOpinion.SHOULD_GROW) {

        } else if (result == PoolSizeOpinion.SHOULD_SHRINK) {

        }
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
            pool.addTaskInternal(inspectPoolQueueSizeTask, true);
        }
    }
}

