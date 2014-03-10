package com.messaggi.workflow;

import java.util.UUID;

public interface MessagingWorkflowFactory
{
    MessagingWorkflow create(UUID applicationPlatformToken) throws Exception;
}

