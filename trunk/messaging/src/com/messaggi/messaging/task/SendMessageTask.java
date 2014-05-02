package com.messaggi.messaging.task;

import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
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
            this.msgConnection.sendMessage(this.request);
        } catch (SendMessageException e) {

        }
    }
}

