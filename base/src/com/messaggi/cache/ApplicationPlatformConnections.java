package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.messaggi.external.connection.MessagingServiceConnection;

public class ApplicationPlatformConnections
{
    private static final ApplicationPlatformConnectionsCache CACHE;

    private static final String CACHE_JNDI_NAME = "messaggi:/cache/ApplicationPlatformConnectionsCache";

    static {
        try {
            CACHE = (ApplicationPlatformConnectionsCache) InitialContext.doLookup(CACHE_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + CACHE_JNDI_NAME, e);
        }
    }

    public static MessagingServiceConnection getConnection(Integer applicationPlatformId, String fromDeviceCode,
            String toDeviceCode) throws ExecutionException
    {
        return CACHE.getConnection(applicationPlatformId, fromDeviceCode, toDeviceCode);
    }
}

