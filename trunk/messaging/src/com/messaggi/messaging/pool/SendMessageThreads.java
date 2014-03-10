package com.messaggi.messaging.pool;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SendMessageThreads
{
    private static final String POOL_JNDI_NAME = "messaggi:/pool/SendMessageThreadPool";

    private static final SendMessageThreadPool pool;

    static {
        try {
            pool = (SendMessageThreadPool) InitialContext.doLookup(POOL_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + POOL_JNDI_NAME, e);
        }
    }

    //public static void sendMessage(ApplicationPlatform appPlat, Device from, Device to, Message msg) throws Exception
    //{
    //}

    public static void shutdownAndAwaitTermination() throws Exception
    {
    }
}

