package com.messaggi.workflow;

import com.messaggi.domain.ApplicationPlatform;

public interface MessagingWorkflow
{
    void setApplicationPlatform(ApplicationPlatform applicationPlatform);

    ApplicationPlatform getApplicationPlatform();
}

