package com.messaggi.messaging.pool;

import com.messaggi.messages.SendMessageRequest;

public interface SendMessageThreadPool
{
    void sendMessage(SendMessageRequest request) throws Exception;
}

