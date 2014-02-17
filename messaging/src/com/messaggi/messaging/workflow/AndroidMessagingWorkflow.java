package com.messaggi.messaging.workflow;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.workflow.MessagingWorkflow;

public class AndroidMessagingWorkflow implements MessagingWorkflow
{
    private ApplicationPlatform applicationPlatform;

    // This class will do the work of sending messages by queuing tasks in the necessary thread pools.
    // One part of this workflow is sending the messages.  
    // A second part of this workflow is handling exceptions that are raised as part of message send.

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

