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
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.connection.MessagingServiceConnections;

public class ApplicationPlatformConnectionsCacheImpl implements ApplicationPlatformConnectionsCache
{
    static final int DEFAULT_NUMBER_OF_CONNECTIONS = 2;

    private static final ApplicationPlatformDAO applicationPlatformsDao;

    private static final CacheLoader<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> applicationPlatformCacheLoader;

    private static final CacheLoader<ConnectionKey, MessagingServiceConnection> connectionCacheLoader;

    private LoadingCache<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> cache;

    static {
        applicationPlatformsDao = new ApplicationPlatformDAO();
        connectionCacheLoader = createConnectionCacheLoader();
        applicationPlatformCacheLoader = createApplicationPlatformCacheLoader();
    }

    public ApplicationPlatformConnectionsCacheImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private static CacheLoader<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> createApplicationPlatformCacheLoader()
    {
        CacheLoader<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> cacheLoader = new CacheLoader<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>>()
        {
            private LoadingCache<ConnectionKey, MessagingServiceConnection> createConnectionCache(ApplicationPlatform ap)
                throws Exception
            {
                //TODO: get the maximum size for this application platforms' connection cache from the domain object.
                int maxSize = 100;
                //TODO: get the record stats for this application platforms' connection cache from the domain object.
                boolean isRecordStats = true;
                CacheBuilder<Object, Object> connectionCacheBuilder = CacheBuilder.newBuilder().maximumSize(maxSize);
                if (isRecordStats) {
                    connectionCacheBuilder.recordStats();
                }
                LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache = connectionCacheBuilder
                        .build(connectionCacheLoader);
                //TODO: get the number of connections to spin up automatically for this 
                //application platform from the domain object.
                int numberOfConnections = DEFAULT_NUMBER_OF_CONNECTIONS;
                for (int connectionIndex = 0; connectionIndex < numberOfConnections; connectionIndex++) {
                    connectionCache.get(new ConnectionKey(ap.getId(), connectionIndex));
                }
                return connectionCache;
            }

            @Override
            public LoadingCache<ConnectionKey, MessagingServiceConnection> load(Integer id) throws Exception
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
            public Map<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> loadAll(
                    Iterable<? extends Integer> ids)
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
                Map<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> retrievedMap = null;
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

    private static CacheLoader<ConnectionKey, MessagingServiceConnection> createConnectionCacheLoader()
    {
        CacheLoader<ConnectionKey, MessagingServiceConnection> cacheLoader = new CacheLoader<ConnectionKey, MessagingServiceConnection>()
        {
            @Override
            public MessagingServiceConnection load(ConnectionKey key) throws Exception
            {
                return createConnection(key);
            }

            @Override
            public Map<ConnectionKey, MessagingServiceConnection> loadAll(Iterable<? extends ConnectionKey> keys)
                throws Exception
            {
                Map<ConnectionKey, MessagingServiceConnection> createdConnectionMap = new HashMap<>();
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

    private static MessagingServiceConnection createConnection(ConnectionKey key) throws Exception
    {
        ApplicationPlatform applicationPlatform = ApplicationPlatforms.get(key.applicationPlatformId);
        return MessagingServiceConnections.create(applicationPlatform);
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
    private int computeConnectionId(long connectionCacheSize, String from, String to)
    {
        int hashCode = (from + to).hashCode();
        int connectionId = (int) (hashCode % connectionCacheSize);
        return Math.abs(connectionId);
    }

    //TODO: The cache values will be connection interfaces that will be implemented by the apple 
    // and android connection implementation objects.
    @Override
    public MessagingServiceConnection getConnection(Integer applicationPlatformId, String fromDeviceCode,
            String toDeviceCode) throws ExecutionException
    {
        LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache = cache.get(applicationPlatformId);
        int connectionId = computeConnectionId(connectionCache.size(), fromDeviceCode, toDeviceCode);
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
            if (!(obj instanceof ConnectionKey))
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

