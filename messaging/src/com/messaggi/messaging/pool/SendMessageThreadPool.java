package com.messaggi.messaging.pool;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public interface SendMessageThreadPool
{
    void sendMessage(ApplicationPlatform applicationPlatform, String messageText, Device from, Device... to)
        throws Exception;
}

