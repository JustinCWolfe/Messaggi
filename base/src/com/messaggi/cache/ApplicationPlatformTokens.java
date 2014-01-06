package com.messaggi.cache;

import java.lang.reflect.Constructor;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;

/**
 * Maps application platform tokens to application platform ids. Tokens are
 * passed-in the client applications but internally I use application platform
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
                String cacheImplClassName = (String) InitialContext.doLookup(CACHE_IMPL_CLASS_JNDI_NAME);
                Class<?> clazz = Class.forName(cacheImplClassName);
                Constructor<?> ctor = clazz.getDeclaredConstructor(new Class[0]);
                ctor.setAccessible(true);
                instance = (ApplicationPlatformTokens) ctor.newInstance();
            }
            return instance;
        }
    }

    public Integer get(UUID token) throws ExecutionException;

    public void initialize(CacheInitializationParameters initParams);
}

