package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import com.messaggi.domain.Device;

/**
 * Cache of devices per application platform. This is a multi-level cache where
 * the first level is for application platforms and the second level is for
 * devices. To get a device you must do so through the relevant application
 * platform.
 */
public interface ApplicationPlatformDevices
{
    public static class Instance
    {
        private static final String CACHE_IMPL_CLASS_JNDI_NAME = "java:/comp/env/ApplicationPlatformDevicesCacheImpl";

        private static final Object lock = new Object();

        private static volatile ApplicationPlatformDevices instance;

        public static ApplicationPlatformDevices getInstance() throws Exception
        {
            if (instance == null) {
                synchronized (lock) {
                    if (instance == null) {
                        instance = CacheHelper.createInstance(CACHE_IMPL_CLASS_JNDI_NAME);
                    }
                }
            }
            return instance;
        }
    }

    void createDeviceCacheForAllApplicationPlatforms(Iterable<? extends Integer> ids) throws ExecutionException;

    Device getDevice(Integer applicationPlatformId, String deviceCode) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

