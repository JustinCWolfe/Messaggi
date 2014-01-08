package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatformTokensImpl implements ApplicationPlatformTokens
{
    private final static ApplicationPlatformDAO dao;

    private final static CacheLoader<UUID, Integer> cacheLoader;

    private LoadingCache<UUID, Integer> cache;

    static {
        dao = new ApplicationPlatformDAO();
        cacheLoader = createCacheLoader();
    }

    private ApplicationPlatformTokensImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private static CacheLoader<UUID, Integer> createCacheLoader()
    {
        CacheLoader<UUID, Integer> cacheLoader = new CacheLoader<UUID, Integer>()
        {
            @Override
            public Integer load(UUID token) throws Exception
            {
                List<ApplicationPlatform> retrieved = dao
                        .getApplicationPlatform(new ApplicationPlatform[] { createPrototype(token) });
                return (retrieved.size() == 1) ? retrieved.get(0).getId() : null;
            }
            @Override
            public Map<UUID, Integer> loadAll(Iterable<? extends UUID> tokens) throws Exception
            {
                List<ApplicationPlatform> prototypes = new ArrayList<>();
                for (UUID token : tokens) {
                    prototypes.add(createPrototype(token));
                }
                List<ApplicationPlatform> retrieved = dao.getApplicationPlatform(prototypes
                        .toArray(new ApplicationPlatform[prototypes.size()]));
                Map<UUID, Integer> retrievedMap = null;
                if (retrieved.size() > 0) {
                    retrievedMap = new HashMap<>();
                    for (ApplicationPlatform ap : retrieved) {
                        retrievedMap.put(ap.getToken(), ap.getId());
                    }
                }
                return retrievedMap;
            }
        };
        return cacheLoader;
    }

    private static ApplicationPlatform createPrototype(UUID token)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setToken(token);
        return prototype;
    }

    @Override
    public Integer get(UUID token) throws ExecutionException
    {
        return cache.get(token);
    }

    @Override
    public ImmutableMap<UUID, Integer> getAll(Iterable<? extends UUID> tokens) throws ExecutionException
    {
        return cache.getAll(tokens);
    }

    @Override
    public void initialize(CacheInitializationParameters initParams)
    {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().maximumSize(initParams.getMaxSize());
        if (initParams.isRecordStats()) {
            builder.recordStats();
        }
        cache = builder.build(cacheLoader);
    }
}

