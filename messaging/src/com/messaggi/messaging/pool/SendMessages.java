package com.messaggi.messaging.pool;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class SendMessages
{
    private static final SendMessageThreadPool POOL;

    private static final String POOL_JNDI_NAME = "messaggi:/pool/SendMessageThreadPool";

    static {
        try {
            POOL = (SendMessageThreadPool) InitialContext.doLookup(POOL_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + POOL_JNDI_NAME, e);
        }
    }

    public static void sendMessage(ApplicationPlatform applicationPlatform, String messageText, Device from,
            Device... to) throws Exception
    {
        POOL.sendMessage(applicationPlatform, messageText, from, to);
    }
}

