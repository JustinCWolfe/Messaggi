package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatformConnectionsImpl implements ApplicationPlatformConnections
{
    private static final ApplicationPlatformDAO applicationPlatformsDao;

    private static final CacheLoader<Integer, LoadingCache<ConnectionKey, Object>> applicationPlatformCacheLoader;

    private static final CacheLoader<ConnectionKey, Object> connectionCacheLoader;

    private LoadingCache<Integer, LoadingCache<ConnectionKey, Object>> cache;

    static {
        applicationPlatformsDao = new ApplicationPlatformDAO();
        connectionCacheLoader = createConnectionCacheLoader();
        applicationPlatformCacheLoader = createApplicationPlatformCacheLoader();
    }

    private ApplicationPlatformConnectionsImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private static CacheLoader<Integer, LoadingCache<ConnectionKey, Object>> createApplicationPlatformCacheLoader()
    {
        CacheLoader<Integer, LoadingCache<ConnectionKey, Object>> cacheLoader = new CacheLoader<Integer, LoadingCache<ConnectionKey, Object>>()
        {
            private LoadingCache<ConnectionKey, Object> createConnectionCache(ApplicationPlatform ap) throws Exception
            {
                //TODO: get the maximum size for this application platforms' connection cache from the domain object.
                int maxSize = 100;
                //TODO: get the record stats for this application platforms' connection cache from the domain object.
                boolean isRecordStats = true;
                CacheBuilder<Object, Object> connectionCacheBuilder = CacheBuilder.newBuilder().maximumSize(maxSize);
                if (isRecordStats) {
                    connectionCacheBuilder.recordStats();
                }
                LoadingCache<ConnectionKey, Object> connectionCache = connectionCacheBuilder
                        .build(connectionCacheLoader);
                //TODO: get the number of connections to spin up automatically for this 
                //application platform from the domain object.
                int numberOfConnections = 2;
                for (int connectionIndex = 1; connectionIndex <= numberOfConnections; connectionIndex++) {
                    connectionCache.get(new ConnectionKey(ap.getId(), connectionIndex));
                }
                return connectionCache;
            }

            @Override
            public LoadingCache<ConnectionKey, Object> load(Integer id) throws Exception
            {
                // What this should do is load the application platform.  This application platform could have 
                // configuration options in attribute columns (these aren't there today but I might add them so 
                // that applications can have connection caching specific to their needs). 
                List<ApplicationPlatform> retrieved = applicationPlatformsDao
                        .getApplicationPlatform(new ApplicationPlatform[] { createApplicationPlatformPrototype(id) });
                if (retrieved.size() == 1) {
                    return createConnectionCache(retrieved.get(0));
                }
                return null;
            }

            @Override
            public Map<Integer, LoadingCache<ConnectionKey, Object>> loadAll(Iterable<? extends Integer> ids)
                throws Exception
            {
                List<ApplicationPlatform> prototypes = new ArrayList<>();
                for (Integer id : ids) {
                    prototypes.add(createApplicationPlatformPrototype(id));
                }
                // What this should do is load the application platform.  This application platform could have 
                // configuration options in attribute columns (these aren't there today but I might add them so 
                // that applications can have connection caching specific to their needs). 
                List<ApplicationPlatform> retrieved = applicationPlatformsDao.getApplicationPlatform(prototypes
                        .toArray(new ApplicationPlatform[prototypes.size()]));
                Map<Integer, LoadingCache<ConnectionKey, Object>> retrievedMap = null;
                if (retrieved.size() > 0) {
                    retrievedMap = new HashMap<>();
                    for (ApplicationPlatform ap : retrieved) {
                        retrievedMap.put(ap.getId(), createConnectionCache(ap));
                    }
                }
                return retrievedMap;
            }
        };
        return cacheLoader;
    }

    private static CacheLoader<ConnectionKey, Object> createConnectionCacheLoader()
    {
        CacheLoader<ConnectionKey, Object> cacheLoader = new CacheLoader<ConnectionKey, Object>()
        {
            @Override
            public Object load(ConnectionKey key) throws Exception
            {
                return createConnection(key);
            }

            @Override
            public Map<ConnectionKey, Object> loadAll(Iterable<? extends ConnectionKey> keys) throws Exception
            {
                Map<ConnectionKey, Object> createdConnectionMap = new HashMap<>();
                for (ConnectionKey key : keys) {
                    createdConnectionMap.put(key, createConnection(key));
                }
                return createdConnectionMap;
            }
        };
        return cacheLoader;
    }

    private static ApplicationPlatform createApplicationPlatformPrototype(Integer id)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setId(id);
        return prototype;
    }

    private static Object createConnection(ConnectionKey key)
    {
        return null;
    }
    
    @Override
    public void createConnectionCacheForAllApplicationPlatforms(Iterable<? extends Integer> ids)
        throws ExecutionException
    {
        cache.getAll(ids);
    }

    // Compute the connection id based on the size of the connection cache and the from device code
    // and to device code.  This must be stable so that it always returns the same external 
    // service connection for each request.
    private Integer computeConnectionId(long connectionCacheSize, String from, String to)
    {
        return 1;
    }

    //TODO: The cache values will be connection interfaces that will be implemented by the apple 
    // and android connection implementation objects.
    @Override
    public Object getConnection(Integer applicationPlatformId, String fromDeviceCode, String toDeviceCode)
        throws ExecutionException
    {
        LoadingCache<ConnectionKey, Object> connectionCache = cache.get(applicationPlatformId);
        Integer connectionId = computeConnectionId(connectionCache.size(), fromDeviceCode, toDeviceCode);
        // Calling getIfPresent will never cause values to be loaded.
        return connectionCache.getIfPresent(new ConnectionKey(applicationPlatformId, connectionId));
    }

    @Override
    public void initialize(CacheInitializationParameters initParams)
    {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().maximumSize(initParams.getMaxSize());
        if (initParams.isRecordStats()) {
            builder.recordStats();
        }
        cache = builder.build(applicationPlatformCacheLoader);
    }
    
    public static class ConnectionKey
    {
        private final Integer applicationPlatformId;

        private final Integer connectionId;

        public ConnectionKey(Integer applicationPlatformId, Integer connectionId)
        {
            this.applicationPlatformId = applicationPlatformId;
            this.connectionId = connectionId;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((applicationPlatformId == null) ? 0 : applicationPlatformId.hashCode());
            result = prime * result + ((connectionId == null) ? 0 : connectionId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ConnectionKey other = (ConnectionKey) obj;
            if (applicationPlatformId == null) {
                if (other.applicationPlatformId != null)
                    return false;
            } else if (!applicationPlatformId.equals(other.applicationPlatformId))
                return false;
            if (connectionId == null) {
                if (other.connectionId != null)
                    return false;
            } else if (!connectionId.equals(other.connectionId))
                return false;
            return true;
        }
    }
}
