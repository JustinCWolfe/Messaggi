package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;

public class AmazonConnection implements MessagingServiceConnection
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
    public void connect()
    {

    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request)
    {
        return new SendMessageResponse();
    }
}

