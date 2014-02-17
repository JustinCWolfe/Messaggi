package com.messaggi.workflow;

import java.util.UUID;

import com.messaggi.util.JNDIHelper;

public interface MessagingWorkflowFactory
{
    public static class Instance
    {
        private static final String FACTORY_IMPL_CLASS_JNDI_NAME = "java:/comp/env/MessagingWorkflowFactoryImpl";

        private static final Object lock = new Object();

        private static volatile MessagingWorkflowFactory instance;

        public static MessagingWorkflowFactory getInstance() throws Exception
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
    
    MessagingWorkflow create(UUID applicationPlatformToken) throws Exception;
}

