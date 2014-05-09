package com.messaggi.external.connection;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.SendMessageResponse;
import com.messaggi.external.message.exception.SendMessageException;

public interface MessagingServiceConnection
{
    void setApplicationPlatform(ApplicationPlatform applicationPlatform);

    ApplicationPlatform getApplicationPlatform();

    void connect() throws Exception;

    void disconnect() throws Exception;

    SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException;
}

