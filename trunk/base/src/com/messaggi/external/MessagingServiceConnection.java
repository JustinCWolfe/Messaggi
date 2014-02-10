package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;

public interface MessagingServiceConnection
{
    void setApplicationPlatform(ApplicationPlatform applicationPlatform);

    ApplicationPlatform getApplicationPlatform();

    void connect() throws Exception;

    SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException;
}

