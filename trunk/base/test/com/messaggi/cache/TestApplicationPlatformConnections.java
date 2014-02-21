package com.messaggi.cache;

import static com.messaggi.cache.ApplicationPlatformConnectionsImpl.DEFAULT_NUMBER_OF_CONNECTIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.messaggi.cache.ApplicationPlatformConnectionsImpl.ConnectionKey;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;

public class TestApplicationPlatformConnections extends ApplicationPlatformCacheTestCase
{
    private LoadingCache<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> cache;

    @Override
    @Before
    public void setUp() throws Exception
    {
        CacheInitializationParameters cip = new CacheInitializationParameters(3, true);
        ApplicationPlatformConnections.Instance.getInstance().initialize(cip);
        createCacheReference();
    }

    @SuppressWarnings("unchecked")
    private void createCacheReference() throws Exception
    {
        Field cacheField = getCacheField(ApplicationPlatformConnectionsImpl.class);
        cache = (LoadingCache<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>>) cacheField
                .get(ApplicationPlatformConnections.Instance.getInstance());
    }

    private LoadingCache<ConnectionKey, MessagingServiceConnection> getConnectionCacheReferenceForApplicationPlatform(
            Integer id) throws Exception
    {
        return cache.get(id);
    }

    @Test
    public void testGetConnection() throws Exception
    {
        List<ApplicationPlatform> appPlats = new ArrayList<>();
        appPlats.add(appPlat1);
        appPlats.add(appPlat2);
        appPlats.add(appPlat3);

        int hits = 0;
        int requests = 0;
        int numberOfFromToPairs = 1000;

        for (int appPlatIndex = 0; appPlatIndex < appPlats.size(); appPlatIndex++) {
            ApplicationPlatform appPlat = appPlats.get(appPlatIndex);
            for (int sendReceiveIndex = 0; sendReceiveIndex < numberOfFromToPairs; sendReceiveIndex++) {
                String from = RandomStringUtils.random(10 + sendReceiveIndex);
                String to = RandomStringUtils.random(10 + sendReceiveIndex);
                MessagingServiceConnection conn = ApplicationPlatformConnections.Instance.getInstance().getConnection(
                        appPlat.getId(), from, to);
                assertNotNull(conn);
                assertEquals(appPlat.getId(), conn.getApplicationPlatform().getId());
            }
            hits += (numberOfFromToPairs - 1);
            requests += numberOfFromToPairs;
        }

        assertEquals(3, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map1 = cache.asMap();
        assertEquals(3, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));
        assertTrue(map1.containsKey(appPlat2.getId()));
        assertTrue(map1.containsKey(appPlat3.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(hits, stats1.hitCount());
        assertEquals(hits / (double) requests, stats1.hitRate(), EPSILON);
        assertEquals(3, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0.0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(3, stats1.loadSuccessCount());
        assertEquals(requests - hits, stats1.missCount());
        assertEquals((requests - hits) / (double) requests, stats1.missRate(), EPSILON);
        assertEquals(requests, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        List<LoadingCache<ConnectionKey, MessagingServiceConnection>> connectionCaches1 = new ArrayList<>();
        connectionCaches1.add(getConnectionCacheReferenceForApplicationPlatform(appPlat1.getId()));
        hits++;
        requests++;
        connectionCaches1.add(getConnectionCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        connectionCaches1.add(getConnectionCacheReferenceForApplicationPlatform(appPlat3.getId()));
        hits++;
        requests++;
        for (LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache : connectionCaches1) {
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionCache.size());

            ConcurrentMap<ConnectionKey, MessagingServiceConnection> connectionMap = connectionCache.asMap();
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionMap.size());

            long connectionLastLoadTime = 0;
            CacheStats connectionStats = connectionCache.stats();
            assertEquals(0, connectionStats.evictionCount());
            assertEquals(numberOfFromToPairs, connectionStats.hitCount());
            assertEquals(numberOfFromToPairs / (double) (DEFAULT_NUMBER_OF_CONNECTIONS + numberOfFromToPairs),
                    connectionStats.hitRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadCount());
            assertEquals(0, connectionStats.loadExceptionCount());
            assertEquals(0.0, connectionStats.loadExceptionRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadSuccessCount());
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.missCount());
            assertEquals(
                    DEFAULT_NUMBER_OF_CONNECTIONS / (double) (DEFAULT_NUMBER_OF_CONNECTIONS + numberOfFromToPairs),
                    connectionStats.missRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS + numberOfFromToPairs, connectionStats.requestCount());
            assertTrue(connectionStats.totalLoadTime() > connectionLastLoadTime);
        }
    }

    @Test
    public void testCreateConnectionCacheForAllApplicationPlatforms() throws Exception
    {
        int hits = 0;
        int requests = 0;

        ApplicationPlatformConnections.Instance.getInstance().createConnectionCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));
        requests += 2;

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map1 = cache.asMap();
        assertEquals(2, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));
        assertTrue(map1.containsKey(appPlat2.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(hits, stats1.hitCount());
        assertEquals(hits / (double) requests, stats1.hitRate(), EPSILON);
        assertEquals(1, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0.0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(1, stats1.loadSuccessCount());
        assertEquals(requests - hits, stats1.missCount());
        assertEquals((requests - hits) / (double) requests, stats1.missRate(), EPSILON);
        assertEquals(requests, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        List<LoadingCache<ConnectionKey, MessagingServiceConnection>> connectionCaches1 = new ArrayList<>();
        connectionCaches1.add(getConnectionCacheReferenceForApplicationPlatform(appPlat1.getId()));
        hits++;
        requests++;
        connectionCaches1.add(getConnectionCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        for (LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache : connectionCaches1) {
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionCache.size());

            ConcurrentMap<ConnectionKey, MessagingServiceConnection> connectionMap = connectionCache.asMap();
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionMap.size());

            long connectionLastLoadTime = 0;
            CacheStats connectionStats = connectionCache.stats();
            assertEquals(0, connectionStats.evictionCount());
            assertEquals(0, connectionStats.hitCount());
            assertEquals(0.0, connectionStats.hitRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadCount());
            assertEquals(0, connectionStats.loadExceptionCount());
            assertEquals(0.0, connectionStats.loadExceptionRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadSuccessCount());
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.missCount());
            assertEquals(1.0, connectionStats.missRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.requestCount());
            assertTrue(connectionStats.totalLoadTime() > connectionLastLoadTime);
        }

        ApplicationPlatformConnections.Instance.getInstance().createConnectionCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));
        hits += 2;
        requests += 2;

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map2 = cache.asMap();
        assertEquals(2, map2.size());
        assertTrue(map2.containsKey(appPlat1.getId()));
        assertTrue(map2.containsKey(appPlat2.getId()));

        CacheStats stats2 = cache.stats();
        assertEquals(0, stats2.evictionCount());
        assertEquals(hits, stats2.hitCount());
        assertEquals(hits / (double) requests, stats2.hitRate(), EPSILON);
        assertEquals(1, stats2.loadCount());
        assertEquals(0, stats2.loadExceptionCount());
        assertEquals(0.0, stats2.loadExceptionRate(), EPSILON);
        assertEquals(1, stats2.loadSuccessCount());
        assertEquals(requests - hits, stats2.missCount());
        assertEquals((requests - hits) / (double) requests, stats2.missRate(), EPSILON);
        assertEquals(requests, stats2.requestCount());
        assertEquals(stats2.totalLoadTime(), lastLoadTime);

        List<LoadingCache<ConnectionKey, MessagingServiceConnection>> connectionCaches2 = new ArrayList<>();
        connectionCaches2.add(getConnectionCacheReferenceForApplicationPlatform(appPlat1.getId()));
        hits++;
        requests++;
        connectionCaches2.add(getConnectionCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        for (LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache : connectionCaches2) {
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionCache.size());

            ConcurrentMap<ConnectionKey, MessagingServiceConnection> connectionMap = connectionCache.asMap();
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionMap.size());

            long connectionLastLoadTime = 0;
            CacheStats connectionStats = connectionCache.stats();
            assertEquals(0, connectionStats.evictionCount());
            assertEquals(0, connectionStats.hitCount());
            assertEquals(0.0, connectionStats.hitRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadCount());
            assertEquals(0, connectionStats.loadExceptionCount());
            assertEquals(0.0, connectionStats.loadExceptionRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadSuccessCount());
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.missCount());
            assertEquals(1.0, connectionStats.missRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.requestCount());
            assertTrue(connectionStats.totalLoadTime() > connectionLastLoadTime);
        }

        ApplicationPlatformConnections.Instance.getInstance().createConnectionCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat3.getId() }));
        requests++;

        assertEquals(3, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map3 = cache.asMap();
        assertEquals(3, map3.size());
        assertTrue(map3.containsKey(appPlat1.getId()));
        assertTrue(map3.containsKey(appPlat2.getId()));
        assertTrue(map3.containsKey(appPlat3.getId()));

        CacheStats stats3 = cache.stats();
        assertEquals(0, stats3.evictionCount());
        assertEquals(hits, stats3.hitCount());
        assertEquals(hits / (double) requests, stats3.hitRate(), EPSILON);
        assertEquals(2, stats3.loadCount());
        assertEquals(0, stats3.loadExceptionCount());
        assertEquals(0.0, stats3.loadExceptionRate(), EPSILON);
        assertEquals(2, stats3.loadSuccessCount());
        assertEquals(requests - hits, stats3.missCount());
        assertEquals((requests - hits) / (double) requests, stats3.missRate(), EPSILON);
        assertEquals(requests, stats3.requestCount());
        assertTrue(stats3.totalLoadTime() > lastLoadTime);

        List<LoadingCache<ConnectionKey, MessagingServiceConnection>> connectionCaches3 = new ArrayList<>();
        connectionCaches3.add(getConnectionCacheReferenceForApplicationPlatform(appPlat1.getId()));
        hits++;
        requests++;
        connectionCaches3.add(getConnectionCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        connectionCaches3.add(getConnectionCacheReferenceForApplicationPlatform(appPlat3.getId()));
        hits++;
        requests++;
        for (LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache : connectionCaches3) {
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionCache.size());

            ConcurrentMap<ConnectionKey, MessagingServiceConnection> connectionMap = connectionCache.asMap();
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionMap.size());

            long connectionLastLoadTime = 0;
            CacheStats connectionStats = connectionCache.stats();
            assertEquals(0, connectionStats.evictionCount());
            assertEquals(0, connectionStats.hitCount());
            assertEquals(0.0, connectionStats.hitRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadCount());
            assertEquals(0, connectionStats.loadExceptionCount());
            assertEquals(0.0, connectionStats.loadExceptionRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadSuccessCount());
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.missCount());
            assertEquals(1.0, connectionStats.missRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.requestCount());
            assertTrue(connectionStats.totalLoadTime() > connectionLastLoadTime);
        }

        ApplicationPlatformConnections.Instance.getInstance().createConnectionCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat3.getId(), appPlat4.getId() }));
        hits++;
        requests += 2;

        assertEquals(3, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map4 = cache.asMap();
        assertEquals(3, map4.size());
        assertTrue(map4.containsKey(appPlat2.getId()));
        assertTrue(map4.containsKey(appPlat3.getId()));
        assertTrue(map4.containsKey(appPlat4.getId()));

        CacheStats stats4 = cache.stats();
        assertEquals(1, stats4.evictionCount());
        assertEquals(hits, stats4.hitCount());
        assertEquals(hits / (double) requests, stats4.hitRate(), EPSILON);
        assertEquals(3, stats4.loadCount());
        assertEquals(0, stats4.loadExceptionCount());
        assertEquals(0.0, stats4.loadExceptionRate(), EPSILON);
        assertEquals(3, stats4.loadSuccessCount());
        assertEquals(requests - hits, stats4.missCount());
        assertEquals((requests - hits) / (double) requests, stats4.missRate(), EPSILON);
        assertEquals(requests, stats4.requestCount());
        assertTrue(stats4.totalLoadTime() > lastLoadTime);

        List<LoadingCache<ConnectionKey, MessagingServiceConnection>> connectionCaches4 = new ArrayList<>();
        connectionCaches4.add(getConnectionCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        connectionCaches4.add(getConnectionCacheReferenceForApplicationPlatform(appPlat3.getId()));
        hits++;
        requests++;
        connectionCaches4.add(getConnectionCacheReferenceForApplicationPlatform(appPlat4.getId()));
        hits++;
        requests++;
        for (LoadingCache<ConnectionKey, MessagingServiceConnection> connectionCache : connectionCaches4) {
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionCache.size());

            ConcurrentMap<ConnectionKey, MessagingServiceConnection> connectionMap = connectionCache.asMap();
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionMap.size());

            long connectionLastLoadTime = 0;
            CacheStats connectionStats = connectionCache.stats();
            assertEquals(0, connectionStats.evictionCount());
            assertEquals(0, connectionStats.hitCount());
            assertEquals(0.0, connectionStats.hitRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadCount());
            assertEquals(0, connectionStats.loadExceptionCount());
            assertEquals(0.0, connectionStats.loadExceptionRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.loadSuccessCount());
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.missCount());
            assertEquals(1.0, connectionStats.missRate(), EPSILON);
            assertEquals(DEFAULT_NUMBER_OF_CONNECTIONS, connectionStats.requestCount());
            assertTrue(connectionStats.totalLoadTime() > connectionLastLoadTime);
        }
    }

    @Test
    public void testInitialize() throws Exception
    {
        validateCacheInitialState(cache);

        List<ApplicationPlatform> appPlats = new ArrayList<>();
        appPlats.add(appPlat1);
        appPlats.add(appPlat2);
        appPlats.add(appPlat3);

        int hits = 0;
        int requests = 0;
        int numberOfFromToPairs = 1000;

        for (int appPlatIndex = 0; appPlatIndex < appPlats.size(); appPlatIndex++) {
            ApplicationPlatform appPlat = appPlats.get(appPlatIndex);
            for (int sendReceiveIndex = 0; sendReceiveIndex < numberOfFromToPairs; sendReceiveIndex++) {
                String from = RandomStringUtils.random(10 + sendReceiveIndex);
                String to = RandomStringUtils.random(10 + sendReceiveIndex);
                MessagingServiceConnection conn = ApplicationPlatformConnections.Instance.getInstance().getConnection(
                        appPlat.getId(), from, to);
                assertNotNull(conn);
                assertEquals(appPlat.getId(), conn.getApplicationPlatform().getId());
            }
            hits += (numberOfFromToPairs - 1);
            requests += numberOfFromToPairs;
        }

        assertEquals(3, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map1 = cache.asMap();
        assertEquals(3, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));
        assertTrue(map1.containsKey(appPlat2.getId()));
        assertTrue(map1.containsKey(appPlat3.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(hits, stats1.hitCount());
        assertEquals(hits / (double) requests, stats1.hitRate(), EPSILON);
        assertEquals(3, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0.0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(3, stats1.loadSuccessCount());
        assertEquals(requests - hits, stats1.missCount());
        assertEquals((requests - hits) / (double) requests, stats1.missRate(), EPSILON);
        assertEquals(requests, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        CacheInitializationParameters cip = new CacheInitializationParameters(2, true);
        ApplicationPlatformConnections.Instance.getInstance().initialize(cip);
        createCacheReference();

        assertEquals(0, cache.size());

        ConcurrentMap<Integer, LoadingCache<ConnectionKey, MessagingServiceConnection>> map2 = cache.asMap();
        assertEquals(0, map2.size());

        CacheStats stats2 = cache.stats();
        assertEquals(0, stats2.evictionCount());
        assertEquals(0, stats2.hitCount());
        assertEquals(1.0, stats2.hitRate(), EPSILON);
        assertEquals(0, stats2.loadCount());
        assertEquals(0, stats2.loadExceptionCount());
        assertEquals(0, stats2.loadExceptionRate(), EPSILON);
        assertEquals(0, stats2.loadSuccessCount());
        assertEquals(0, stats2.missCount());
        assertEquals(0.0, stats2.missRate(), EPSILON);
        assertEquals(0, stats2.requestCount());
        assertTrue(stats2.totalLoadTime() == 0);
    }
}

