package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.messages.SendMessageRequest;

public class AndroidConnection implements MessagingServiceConnection
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
    public boolean sendMessage(SendMessageRequest request)
    {
        return false;
    }
}

