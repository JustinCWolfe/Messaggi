package com.messaggi.messaging.external.connection;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.SendMessageResponse;
import com.messaggi.external.message.exception.SendMessageException;

public class WindowsConnection implements MessagingServiceConnection
{
    private ApplicationPlatform applicationPlatform;

    @Override
    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    @Override
    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    @Override
    public void connect() throws Exception
    {

    }

    @Override
    public void disconnect() throws Exception
    {

    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        return new SendMessageResponse();
    }
}

