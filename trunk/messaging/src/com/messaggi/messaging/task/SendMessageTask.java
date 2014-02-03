package com.messaggi.messaging.task;

import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageRequest;

public class SendMessageTask implements Runnable
{
    private final MessagingServiceConnection msgConnection;

    private final SendMessageRequest request;

    public SendMessageTask(MessagingServiceConnection msgConnection, SendMessageRequest request)
    {
        this.msgConnection = msgConnection;
        this.request = request;
    }

    @Override
    public void run()
    {
        msgConnection.sendMessage(request);
    }
}

