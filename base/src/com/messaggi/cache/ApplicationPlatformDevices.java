package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import com.messaggi.cache.ApplicationPlatformDevicesImpl.DeviceKey;
import com.messaggi.domain.Device;

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

    Device getDevice(DeviceKey key) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

