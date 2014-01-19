package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;

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
    public void connect()
    {

    }
}

