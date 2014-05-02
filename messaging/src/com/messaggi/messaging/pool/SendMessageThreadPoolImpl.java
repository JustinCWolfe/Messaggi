package com.messaggi.messaging.pool;

import com.messaggi.messages.SendMessageRequest;
import com.messaggi.pool.AutoResizingThreadPool;

public class SendMessageThreadPoolImpl extends AutoResizingThreadPool implements SendMessageThreadPool
{
    private static final String THREAD_NAME_PREFIX = "SendMessageThreadPool";

    public SendMessageThreadPoolImpl()
    {
        super(THREAD_NAME_PREFIX);
    }

    @Override
    public void sendMessage(SendMessageRequest request) throws Exception
    {
    }
}

