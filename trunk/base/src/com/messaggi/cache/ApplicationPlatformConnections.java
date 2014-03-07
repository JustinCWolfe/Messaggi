package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.messaggi.external.MessagingServiceConnection;

public class ApplicationPlatformConnections
{
    private static final String CACHE_JNDI_NAME = "messaggi:/cache/ApplicationPlatformsCache";

    private static final ApplicationPlatformConnectionsCache cache;

    static {
        try {
            cache = (ApplicationPlatformConnectionsCache) InitialContext.doLookup(CACHE_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find crd:cache/Broker: " + e);
        }
    }

    public static MessagingServiceConnection getConnection(Integer applicationPlatformId, String fromDeviceCode,
            String toDeviceCode) throws ExecutionException
    {
        return cache.getConnection(applicationPlatformId, fromDeviceCode, toDeviceCode);
    }
}

