package com.messaggi.messaging.workflow;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.workflow.MessagingWorkflow;

public class AppleMessagingWorkflow implements MessagingWorkflow
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
}

