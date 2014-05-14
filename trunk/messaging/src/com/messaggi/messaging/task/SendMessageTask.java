package com.messaggi.messaging.task;

import com.messaggi.domain.Device;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.exception.SendMessageException;
import com.messaggi.pool.task.TaskBase;

public class SendMessageTask extends TaskBase<Boolean>
{
    private static final String TO_DELIMITER = ",";

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
        return String.format("%s - %s (%s => %s)", this.getClass().getSimpleName(), getApplicationPlatformId(),
                getFrom(), getTo());
    }

    public String getApplicationPlatformId()
    {
        return Long.toString(msgConnection.getApplicationPlatform().getId());
    }

    public String getFrom()
    {
        return request.from.getCode();
    }

    public SendMessageRequest getRequest()
    {
        return request;
    }

    @Override
    protected Boolean getTaskResult()
    {
        // Return true signifying that the task was run.
        return true;
    }

    public String getTo()
    {
        StringBuilder sb = new StringBuilder();
        for (Device to : request.to) {
            sb.append(to.getCode());
            sb.append(TO_DELIMITER);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
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

