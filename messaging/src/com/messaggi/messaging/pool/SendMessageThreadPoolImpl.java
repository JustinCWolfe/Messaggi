package com.messaggi.messaging.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class SendMessageThreadPoolImpl implements SendMessageThreadPool, ThreadFactory
{
    private static final String POOL_THREAD_NAME = "SendMessageThread";

    private static final int SECONDS_TO_WAIT_FOR_RUNNING_THREADS = 60;

    private final ExecutorService pool;

    public SendMessageThreadPoolImpl()
    {
        pool = Executors.newCachedThreadPool(this);
    }

    @Override
    public Thread newThread(Runnable target)
    {
        Thread thread = new Thread(target);
        thread.setName(POOL_THREAD_NAME);
        return thread;
    }

    //@Override
    //public void sendMessage(ApplicationPlatform appPlat, Device from, Device to, Message msg) throws Exception
    //{
    //    MessagingServiceConnection msgConnection = ApplicationPlatformConnections.Instance.getInstance().getConnection(
    //            appPlat.getId(), from.getCode(), to.getCode());
    //    pool.execute(new SendMessageTask(msgConnection, new SendMessageRequest(from, to, msg)));
    //}

    @Override
    public void shutdownAndAwaitTermination() throws Exception
    {
        // Disable new tasks from being submitted
        pool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(SECONDS_TO_WAIT_FOR_RUNNING_THREADS, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks
                pool.shutdownNow();
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(SECONDS_TO_WAIT_FOR_RUNNING_THREADS, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}

