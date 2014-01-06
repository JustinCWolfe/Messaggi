package com.messaggi.cache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Maps application platform tokens to application platform ids. Tokens are
 * passed-in the client applications but internally we use application platform
 * ids.
 */
public interface ApplicationPlatformTokens
{
    public static class Instance
    {
        private static ApplicationPlatformTokens instance;

        private static final String CACHE_IMPL_CLASS_JNDI_NAME = "java:/comp/env/ApplicationPlatformTokensCacheImpl";

        public static synchronized ApplicationPlatformTokens getInstance() throws Exception
        {
            if (instance == null) {
                instance = CacheHelper.createInstance(CACHE_IMPL_CLASS_JNDI_NAME);
            }
            return instance;
        }
    }

    public Integer get(UUID token) throws ExecutionException;

    public void initialize(CacheInitializationParameters initParams);
}

