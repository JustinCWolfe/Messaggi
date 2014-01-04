package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

/**
 * Maps application platform tokens to application platform ids. Tokens are
 * passed-in the client applications but internally I use application platform
 * ids.
 */
public class ApplicationPlatformTokens
{
    private static ApplicationPlatformTokens instance;

    private final LoadingCache<UUID, Integer> applicationPlatformTokenCache;

    private final ApplicationPlatformDAO dao = new ApplicationPlatformDAO();

    public static synchronized ApplicationPlatformTokens getInstance()
    {
        if (instance == null) {
            instance = new ApplicationPlatformTokens();
        }
        return instance;
    }

    private ApplicationPlatformTokens()
    {
        applicationPlatformTokenCache = createApplicationPlatformTokenCache();
    }

    private LoadingCache<UUID, Integer> createApplicationPlatformTokenCache()
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
        return CacheBuilder.newBuilder().maximumSize(1000).build(tokenCacheLoader);
    }

    private ApplicationPlatform createPrototype(UUID token)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setToken(token);
        return prototype;
    }
}

