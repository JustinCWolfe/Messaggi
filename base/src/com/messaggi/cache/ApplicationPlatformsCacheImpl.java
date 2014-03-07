package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatformsCacheImpl implements ApplicationPlatformsCache
{
    private static final ApplicationPlatformDAO dao;

    private static final CacheLoader<Integer, ApplicationPlatform> cacheLoader;

    private LoadingCache<Integer, ApplicationPlatform> cache;

    static {
        dao = new ApplicationPlatformDAO();
        cacheLoader = createCacheLoader();
    }

    private ApplicationPlatformsCacheImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private static CacheLoader<Integer, ApplicationPlatform> createCacheLoader()
    {
        CacheLoader<Integer, ApplicationPlatform> cacheLoader = new CacheLoader<Integer, ApplicationPlatform>()
        {
            @Override
            public ApplicationPlatform load(Integer id) throws Exception
            {
                List<ApplicationPlatform> retrieved = dao
                        .getApplicationPlatform(new ApplicationPlatform[] { createPrototype(id) });
                return (retrieved.size() == 1) ? retrieved.get(0) : null;
            }

            @Override
            public Map<Integer, ApplicationPlatform> loadAll(Iterable<? extends Integer> ids) throws Exception
            {
                List<ApplicationPlatform> prototypes = new ArrayList<>();
                for (Integer id : ids) {
                    prototypes.add(createPrototype(id));
                }
                List<ApplicationPlatform> retrieved = dao.getApplicationPlatform(prototypes
                        .toArray(new ApplicationPlatform[prototypes.size()]));
                Map<Integer, ApplicationPlatform> retrievedMap = null;
                if (retrieved.size() > 0) {
                    retrievedMap = new HashMap<>();
                    for (ApplicationPlatform ap : retrieved) {
                        retrievedMap.put(ap.getId(), ap);
                    }
                }
                return retrievedMap;
            }
        };
        return cacheLoader;
    }

    private static ApplicationPlatform createPrototype(Integer id)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setId(id);
        return prototype;
    }

    @Override
    public ApplicationPlatform get(Integer id) throws ExecutionException
    {
        return cache.get(id);
    }

    @Override
    public ImmutableMap<Integer, ApplicationPlatform> getAll(Iterable<? extends Integer> ids) throws ExecutionException
    {
        return cache.getAll(ids);
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

