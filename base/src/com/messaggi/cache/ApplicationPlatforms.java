package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.common.collect.ImmutableMap;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatforms
{
    private static final ApplicationPlatformsCache CACHE;

    private static final String CACHE_JNDI_NAME = "messaggi:/cache/ApplicationPlatformsCache";

    static {
        try {
            CACHE = (ApplicationPlatformsCache) InitialContext.doLookup(CACHE_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + CACHE_JNDI_NAME, e);
        }
    }

    public static ApplicationPlatform get(Integer id) throws ExecutionException
    {
        return CACHE.get(id);
    }

    public static ImmutableMap<Integer, ApplicationPlatform> getAll(Iterable<? extends Integer> ids)
        throws ExecutionException
    {
        return CACHE.getAll(ids);
    }
}

