package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;
import com.messaggi.domain.ApplicationPlatform;

public interface ApplicationPlatformsCache
{
    ApplicationPlatform get(Integer id) throws ExecutionException;

    ImmutableMap<Integer, ApplicationPlatform> getAll(Iterable<? extends Integer> ids) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

