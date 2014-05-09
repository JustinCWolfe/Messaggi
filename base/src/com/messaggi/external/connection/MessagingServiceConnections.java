package com.messaggi.external.connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.messaggi.domain.ApplicationPlatform;

public class MessagingServiceConnections
{
    private static final MessagingServiceConnectionFactory FACTORY;

    private static final String FACTORY_JNDI_NAME = "messaggi:/factory/MessagingServiceConnectionFactory";

    static {
        try {
            FACTORY = (MessagingServiceConnectionFactory) InitialContext.doLookup(FACTORY_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + FACTORY_JNDI_NAME, e);
        }
    }

    public static MessagingServiceConnection create(ApplicationPlatform applicationPlatform) throws Exception
    {
        return FACTORY.create(applicationPlatform);
    }
}

