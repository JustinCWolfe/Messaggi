package com.messaggi.cache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;
import com.messaggi.util.JNDIHelper;

/**
 * Maps application platform tokens to application platform ids. Tokens are
 * passed-in the client applications but internally we use application platform
 * ids.
 */
public interface ApplicationPlatformTokens
{
    public static class Instance
    {
        private static final String CACHE_IMPL_CLASS_JNDI_NAME = "java:/comp/env/ApplicationPlatformTokensCacheImpl";

        private static final Object lock = new Object();

        private static volatile ApplicationPlatformTokens instance;

        public static ApplicationPlatformTokens getInstance() throws Exception
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

    Integer get(UUID token) throws ExecutionException;

    ImmutableMap<UUID, Integer> getAll(Iterable<? extends UUID> tokens) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

