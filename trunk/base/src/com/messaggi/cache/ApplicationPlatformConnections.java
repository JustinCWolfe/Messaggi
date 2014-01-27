package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.util.JNDIHelper;

/**
 * Cache of messaging service connections per application platform. This is a
 * multi-level cache where the first level is for application platforms and the
 * second level is for messaging service (apple, android, windows, amazon, etc)
 * connections. To get a messaging service connection you must do so through the
 * relevant application platform.
 */
public interface ApplicationPlatformConnections
{
    public static class Instance
    {
        private static final String CACHE_IMPL_CLASS_JNDI_NAME = "java:/comp/env/ApplicationPlatformConnectionsCacheImpl";

        private static final Object lock = new Object();

        private static volatile ApplicationPlatformConnections instance;

        public static ApplicationPlatformConnections getInstance() throws Exception
        {
            if (instance == null) {
                synchronized (lock) {
                    if (instance == null) {
                        instance = JNDIHelper.createInstance(CACHE_IMPL_CLASS_JNDI_NAME);
                    }
                }
            }
            return instance;
        }
    }

    void createConnectionCacheForAllApplicationPlatforms(Iterable<? extends Integer> ids) throws ExecutionException;

    /**
     * Method to establish messaging service connection affinity so that when a
     * given device sends a message to another device, all such traffic will go
     * through the same messaging connection. This will prevent messages from
     * being delivered out of order.
     * 
     * @param applicationPlatformId
     * @param fromDeviceCode
     * @param toDeviceCode
     * @return
     * @throws ExecutionException
     */
    //TODO: The cache values will be connection interfaces that will be implemented by the apple 
    // and android connection implementation objects.
    MessagingServiceConnection getConnection(Integer applicationPlatformId, String fromDeviceCode, String toDeviceCode)
        throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

