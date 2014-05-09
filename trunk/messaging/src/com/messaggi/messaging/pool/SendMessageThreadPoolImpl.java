package com.messaggi.messaging.pool;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.connection.MessagingServiceConnections;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.messaging.task.SendMessageTask;
import com.messaggi.pool.AutoResizingThreadPool;

public class SendMessageThreadPoolImpl extends AutoResizingThreadPool implements SendMessageThreadPool
{
    private static final String THREAD_NAME_PREFIX = "SendMessageThreadPool";

    public SendMessageThreadPoolImpl()
    {
        super(THREAD_NAME_PREFIX);
    }

    @Override
    protected int getTaskNodeIndex()
    {
        //return targetThreadCounter.getAndIncrement() % threadCount;
        return 0;
    }

    @Override
    public void sendMessage(ApplicationPlatform applicationPlatform, String messageText, Device from, Device... to)
        throws Exception
    {
        MessagingServiceConnection msgConnection = MessagingServiceConnections.create(applicationPlatform);
        SendMessageRequest request = new SendMessageRequest(from, to, messageText);
        SendMessageTask task = new SendMessageTask(msgConnection, request);
        addTask(task);
    }
}

