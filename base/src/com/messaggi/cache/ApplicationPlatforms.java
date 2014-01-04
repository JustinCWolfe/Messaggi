package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatforms
{
    private static ApplicationPlatforms instance;

    private final LoadingCache<Integer, ApplicationPlatform> applicationPlatformCache;

    private final ApplicationPlatformDAO dao = new ApplicationPlatformDAO();

    public static synchronized ApplicationPlatforms getInstance()
    {
        if (instance == null) {
            instance = new ApplicationPlatforms();
        }
        return instance;
    }

    private ApplicationPlatforms()
    {
        applicationPlatformCache = createApplicationPlatformCache();
    }

    private LoadingCache<Integer, ApplicationPlatform> createApplicationPlatformCache()
    {
        CacheLoader<Integer, ApplicationPlatform> tokenCacheLoader = new CacheLoader<Integer, ApplicationPlatform>()
        {
            @Override
            public ApplicationPlatform load(Integer id) throws Exception
            {
                List<ApplicationPlatform> retrieved = dao
                        .getApplicationPlatform(new ApplicationPlatform[] { createPrototype(id) });
                return retrieved.get(0);
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
                Map<Integer, ApplicationPlatform> retrievedMap = new HashMap<>();
                for (ApplicationPlatform ap : retrieved) {
                    retrievedMap.put(ap.getId(), ap);
                }
                return retrievedMap;
            }
        };
        return CacheBuilder.newBuilder().maximumSize(1000).build(tokenCacheLoader);
    }

    private ApplicationPlatform createPrototype(Integer id)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setId(id);
        return prototype;
    }
}

