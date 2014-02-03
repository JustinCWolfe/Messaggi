package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.messages.SendMessageRequest;

public class AppleConnection implements MessagingServiceConnection
{
    private ApplicationPlatform applicationPlatform;

    private SendMessageRequest request;

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
    public void setSendMessageRequest(SendMessageRequest request)
    {
        this.request = request;
    }

    @Override
    public void run()
    {

    }
}

