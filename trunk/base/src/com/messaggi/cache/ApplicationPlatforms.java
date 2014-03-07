package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.common.collect.ImmutableMap;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatforms
{
    private static final String CACHE_JNDI_NAME = "messaggi:/cache/ApplicationPlatformsCache";

    private static final ApplicationPlatformsCache cache;

    static {
        try {
            cache = (ApplicationPlatformsCache) InitialContext.doLookup(CACHE_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + CACHE_JNDI_NAME, e);
        }
    }

    public static ApplicationPlatform get(Integer id) throws ExecutionException
    {
        return cache.get(id);
    }

    public static ImmutableMap<Integer, ApplicationPlatform> getAll(Iterable<? extends Integer> ids)
        throws ExecutionException
    {
        return cache.getAll(ids);
    }
}

