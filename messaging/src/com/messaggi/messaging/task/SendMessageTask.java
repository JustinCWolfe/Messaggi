package com.messaggi.messaging.task;

import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.util.Task;

public class SendMessageTask implements Task
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
        try {
            this.msgConnection.sendMessage(this.request);
        } catch (SendMessageException e) {

        }
    }
}

