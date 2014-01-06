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
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatformTokensImpl implements ApplicationPlatformTokens
{
    private final ApplicationPlatformDAO dao = new ApplicationPlatformDAO();

    private LoadingCache<UUID, Integer> applicationPlatformTokenCache;

    private ApplicationPlatformTokensImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private LoadingCache<UUID, Integer> createApplicationPlatformTokenCache(CacheInitializationParameters initParams)
    {
        CacheLoader<UUID, Integer> tokenCacheLoader = new CacheLoader<UUID, Integer>()
        {
            @Override
            public Integer load(UUID token) throws Exception
            {
                List<ApplicationPlatform> retrieved = dao
                        .getApplicationPlatform(new ApplicationPlatform[] { createPrototype(token) });
                return retrieved.get(0).getId();
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
                Map<UUID, Integer> retrievedMap = new HashMap<>();
                for (ApplicationPlatform ap : retrieved) {
                    retrievedMap.put(ap.getToken(), ap.getId());
                }
                return retrievedMap;
            }
        };
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().maximumSize(initParams.getMaxSize());
        if (initParams.isRecordStats()) {
            builder.recordStats();
        }
        return builder.build(tokenCacheLoader);
    }

    private ApplicationPlatform createPrototype(UUID token)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setToken(token);
        return prototype;
    }

    @Override
    public Integer get(UUID token) throws ExecutionException
    {
        return applicationPlatformTokenCache.get(token);
    }

    @Override
    public void initialize(CacheInitializationParameters initParams)
    {
        applicationPlatformTokenCache = createApplicationPlatformTokenCache(initParams);
    }
}

