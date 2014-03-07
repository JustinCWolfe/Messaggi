package com.messaggi.cache;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;

/**
 * Maps application platform tokens to application platform ids. Tokens are
 * passed-in the client applications but internally we use application platform
 * ids.
 */
public interface ApplicationPlatformTokensCache
{
    Integer get(UUID token) throws ExecutionException;

    ImmutableMap<UUID, Integer> getAll(Iterable<? extends UUID> tokens) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

