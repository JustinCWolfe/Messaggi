package com.messaggi.messaging.pool;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.connection.MessagingServiceConnections;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.messaging.pool.task.SendMessageTask;
import com.messaggi.pool.AutoResizingThreadPool;
import com.messaggi.pool.task.Task;

public class SendMessageThreadPoolImpl extends AutoResizingThreadPool implements SendMessageThreadPool
{
    private static final String THREAD_NAME_PREFIX = "SendMessageThreadPool";

    private static final int INITIAL_ODD_NUMBER = 17;

    private static final int MULTIPLIER_ODD_NUMBER = 37;

    public SendMessageThreadPoolImpl()
    {
        super(THREAD_NAME_PREFIX);
    }

    @Override
    protected int getTaskNodeIndex(Task<?> task)
    {
        SendMessageTask sendMessageTask = (SendMessageTask) task;
        String applicationPlatformId = sendMessageTask.getApplicationPlatformId();
        String from = sendMessageTask.getFrom();
        String to = sendMessageTask.getTo();
        // The combination of application platform id, from device code, to device code should 
        // always map to the same thread.
        int taskHashCode = new HashCodeBuilder(INITIAL_ODD_NUMBER, MULTIPLIER_ODD_NUMBER).append(applicationPlatformId)
                .append(from).append(to).toHashCode();
        return taskHashCode % threadCount;
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

