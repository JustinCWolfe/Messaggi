package com.messaggi.cache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.common.collect.ImmutableMap;

public class ApplicationPlatformTokens
{
    private static final ApplicationPlatformTokensCache CACHE;

    private static final String CACHE_JNDI_NAME = "messaggi:/cache/ApplicationPlatformTokensCache";

    static {
        try {
            CACHE = (ApplicationPlatformTokensCache) InitialContext.doLookup(CACHE_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + CACHE_JNDI_NAME, e);
        }
    }

    public static Integer get(UUID token) throws ExecutionException
    {
        return CACHE.get(token);
    }

    public static ImmutableMap<UUID, Integer> getAll(Iterable<? extends UUID> tokens) throws ExecutionException
    {
        return CACHE.getAll(tokens);
    }
}

