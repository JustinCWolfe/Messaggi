package com.messaggi.messaging.task;

import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.exception.SendMessageException;
import com.messaggi.pool.task.TaskBase;

public class SendMessageTask extends TaskBase<Boolean>
{
    private final MessagingServiceConnection msgConnection;

    private final SendMessageRequest request;

    public SendMessageTask(MessagingServiceConnection msgConnection, SendMessageRequest request)
    {
        this.msgConnection = msgConnection;
        this.request = request;
    }

    @Override
    public String getName()
    {
        //return String.format("%s - %s", this.getClass().getSimpleName(), waitTime);
        return null;
    }

    @Override
    protected Boolean getTaskResult()
    {
        // Return true signifying that the task was run.
        return true;
    }

    @Override
    public void runInternal()
    {
        try {
            msgConnection.sendMessage(request);
        } catch (SendMessageException e) {

        }
    }
}

