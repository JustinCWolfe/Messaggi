package com.messaggi.messaging.workflow;

import java.util.UUID;

import com.messaggi.cache.ApplicationPlatformTokens;
import com.messaggi.cache.ApplicationPlatforms;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.workflow.MessagingWorkflow;
import com.messaggi.workflow.MessagingWorkflowFactory;

public class MessagingWorkflowFactoryImpl implements MessagingWorkflowFactory
{
    @Override
    public MessagingWorkflow create(UUID applicationPlatformToken) throws Exception
    {
        Integer id = ApplicationPlatformTokens.get(applicationPlatformToken);
        ApplicationPlatform applicationPlatform = ApplicationPlatforms.get(id);
        MessagingWorkflow messagingWorkflow = null;
        switch (applicationPlatform.getPlatform()) {
            case ANDROID:
                messagingWorkflow = new AndroidMessagingWorkflow();
                break;
            case IOS:
                messagingWorkflow = new AppleMessagingWorkflow();
                break;
            default:
                throw new UnsupportedOperationException();
        }
        messagingWorkflow.setApplicationPlatform(applicationPlatform);
        return messagingWorkflow;
    }
}

