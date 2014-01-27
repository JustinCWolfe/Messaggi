package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.util.JNDIHelper;

public interface ApplicationPlatforms
{
    public static class Instance
    {
        private static final String CACHE_IMPL_CLASS_JNDI_NAME = "java:/comp/env/ApplicationPlatformsCacheImpl";

        private static final Object lock = new Object();

        private static volatile ApplicationPlatforms instance;

        public static ApplicationPlatforms getInstance() throws Exception
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

    ApplicationPlatform get(Integer id) throws ExecutionException;

    ImmutableMap<Integer, ApplicationPlatform> getAll(Iterable<? extends Integer> ids) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

