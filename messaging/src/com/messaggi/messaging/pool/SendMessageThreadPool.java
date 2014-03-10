package com.messaggi.messaging.pool;

public interface SendMessageThreadPool
{
    //void sendMessage(ApplicationPlatform appPlat, Device from, Device to, Message msg) throws Exception;

    void shutdownAndAwaitTermination() throws Exception;
}

