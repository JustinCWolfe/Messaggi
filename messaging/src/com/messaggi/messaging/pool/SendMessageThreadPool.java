package com.messaggi.messaging.pool;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.messages.Message;
import com.messaggi.util.JNDIHelper;

public interface SendMessageThreadPool
{
    public static class Instance
    {
        private static final String SEND_MSG_POOL_IMPL_CLASS_JNDI_NAME = "java:/comp/env/SendMessageThreadPoolImpl";

        private static final Object lock = new Object();

        private static volatile SendMessageThreadPool instance;

        public static SendMessageThreadPool getInstance() throws Exception
        {
            if (instance == null) {
                synchronized (lock) {
                    if (instance == null) {
                        instance = JNDIHelper.createInstance(SEND_MSG_POOL_IMPL_CLASS_JNDI_NAME);
                    }
                }
            }
            return instance;
        }
    }

    void sendMessage(ApplicationPlatform appPlat, Device from, Device to, Message msg) throws Exception;

    void shutdownAndAwaitTermination() throws Exception;
}

