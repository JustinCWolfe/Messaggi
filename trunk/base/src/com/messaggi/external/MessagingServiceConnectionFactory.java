package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.util.JNDIHelper;

public interface MessagingServiceConnectionFactory
{
    public static class Instance
    {
        private static final String FACTORY_IMPL_CLASS_JNDI_NAME = "java:/comp/env/MessagingServiceConnectionFactoryImpl";

        private static final Object lock = new Object();

        private static volatile MessagingServiceConnectionFactory instance;

        public static MessagingServiceConnectionFactory getInstance() throws Exception
        {
            if (instance == null) {
                synchronized (lock) {
                    if (instance == null) {
                        instance = JNDIHelper.createInstance(FACTORY_IMPL_CLASS_JNDI_NAME);
                    }
                }
            }
            return instance;
        }
    }

    MessagingServiceConnection create(ApplicationPlatform applicationPlatform);
}

