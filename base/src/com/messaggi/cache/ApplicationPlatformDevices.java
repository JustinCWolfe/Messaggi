package com.messaggi.cache;


public class ApplicationPlatformDevices
{
    public static class Instance
    {
        private static ApplicationPlatformDevices instance;

        private static final String CACHE_IMPL_CLASS_JNDI_NAME = "java:/comp/env/ApplicationPlatformDevicesCacheImpl";

        public static synchronized ApplicationPlatformDevices getInstance() throws Exception
        {
            if (instance == null) {
                instance = CacheHelper.createInstance(CACHE_IMPL_CLASS_JNDI_NAME);
            }
            return instance;
        }
    }

    //public Integer get(UUID token) throws ExecutionException;

    //public void initialize(CacheInitializationParameters initParams);
}

