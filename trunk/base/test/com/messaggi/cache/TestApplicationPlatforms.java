package com.messaggi.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.messaggi.domain.ApplicationPlatform;

public class TestApplicationPlatforms extends TestApplicationPlatformBase
{
    private LoadingCache<Integer, ApplicationPlatform> cache;

    @Override
    @Before
    public void setUp() throws Exception
    {
        CacheInitializationParameters cip = new CacheInitializationParameters(2, true);
        ApplicationPlatforms.Instance.getInstance().initialize(cip);
        createCacheReference();
    }

    @SuppressWarnings("unchecked")
    private void createCacheReference() throws Exception
    {
        Field cacheField = getCacheField(ApplicationPlatformsImpl.class);
        cache = (LoadingCache<Integer, ApplicationPlatform>) cacheField
                .get(ApplicationPlatforms.Instance.getInstance());
    }

    @Test
    public void testLoadInvalidId() throws Exception
    {
        validateCacheInitialState(cache);

        int nonExistentID = -1;
        try {
            ApplicationPlatforms.Instance.getInstance().get(nonExistentID);
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<Integer, ApplicationPlatform> map = cache.asMap();
            assertEquals(0, map.size());

            long lastLoadTime = 0;
            CacheStats stats = cache.stats();
            assertEquals(0, stats.evictionCount());
            assertEquals(0, stats.hitCount());
            assertEquals(0.0, stats.hitRate(), EPSILON);
            assertEquals(1, stats.loadCount());
            assertEquals(1, stats.loadExceptionCount());
            assertEquals(1.0, stats.loadExceptionRate(), EPSILON);
            assertEquals(0, stats.loadSuccessCount());
            assertEquals(1, stats.missCount());
            assertEquals(1.0, stats.missRate(), EPSILON);
            assertEquals(1, stats.requestCount());
            assertTrue(stats.totalLoadTime() > lastLoadTime);

            return;
        }
        fail("The call above should have thrown and InvalidCacheLoadException");
    }

    @Test
    public void testLoadAllInvalidIds() throws Exception
    {
        validateCacheInitialState(cache);

        Integer nonExistentID1 = -1;
        Integer nonExistentID2 = -2;
        try {
            ApplicationPlatforms.Instance.getInstance().getAll(
                    Arrays.asList(new Integer[] { nonExistentID1, nonExistentID2 }));
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<Integer, ApplicationPlatform> map = cache.asMap();
            assertEquals(0, map.size());

            long lastLoadTime = 0;
            CacheStats stats = cache.stats();
            assertEquals(0, stats.evictionCount());
            assertEquals(0, stats.hitCount());
            assertEquals(0.0, stats.hitRate(), EPSILON);
            assertEquals(1, stats.loadCount());
            assertEquals(1, stats.loadExceptionCount());
            assertEquals(1.0, stats.loadExceptionRate(), EPSILON);
            assertEquals(0, stats.loadSuccessCount());
            assertEquals(2, stats.missCount());
            assertEquals(1.0, stats.missRate(), EPSILON);
            assertEquals(2, stats.requestCount());
            assertTrue(stats.totalLoadTime() > lastLoadTime);

            return;
        }
        fail("The call above should have thrown and InvalidCacheLoadException");
    }

    @Test
    public void testLoadValidId() throws Exception
    {
        validateCacheInitialState(cache);

        ApplicationPlatform appPlat11 = ApplicationPlatforms.Instance.getInstance().get(appPlat1.getId());
        assertEquals(appPlat1.getId(), appPlat11.getId());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map1 = cache.asMap();
        assertEquals(1, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(0, stats1.hitCount());
        assertEquals(0, stats1.hitRate(), EPSILON);
        assertEquals(1, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(1, stats1.loadSuccessCount());
        assertEquals(1, stats1.missCount());
        assertEquals(1.0, stats1.missRate(), EPSILON);
        assertEquals(1, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        // Get the same id - should be returned from the cache instead of loaded.
        ApplicationPlatform appPlat12 = ApplicationPlatforms.Instance.getInstance().get(appPlat1.getId());
        assertEquals(appPlat1.getId(), appPlat12.getId());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map2 = cache.asMap();
        assertEquals(1, map2.size());
        assertTrue(map2.containsKey(appPlat1.getId()));

        CacheStats stats2 = cache.stats();
        assertEquals(0, stats2.evictionCount());
        assertEquals(1, stats2.hitCount());
        assertEquals(.5, stats2.hitRate(), EPSILON);
        assertEquals(1, stats2.loadCount());
        assertEquals(0, stats2.loadExceptionCount());
        assertEquals(0, stats2.loadExceptionRate(), EPSILON);
        assertEquals(1, stats2.loadSuccessCount());
        assertEquals(1, stats2.missCount());
        assertEquals(.5, stats2.missRate(), EPSILON);
        assertEquals(2, stats2.requestCount());
        assertEquals(lastLoadTime, stats2.totalLoadTime());

        // Get another id - should be loaded.
        ApplicationPlatform appPlat21 = ApplicationPlatforms.Instance.getInstance().get(appPlat2.getId());
        assertEquals(appPlat2.getId(), appPlat21.getId());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertTrue(map3.containsKey(appPlat1.getId()));
        assertTrue(map3.containsKey(appPlat2.getId()));

        CacheStats stats3 = cache.stats();
        assertEquals(0, stats3.evictionCount());
        assertEquals(1, stats3.hitCount());
        assertEquals(.333333, stats3.hitRate(), EPSILON);
        assertEquals(2, stats3.loadCount());
        assertEquals(0, stats3.loadExceptionCount());
        assertEquals(0, stats3.loadExceptionRate(), EPSILON);
        assertEquals(2, stats3.loadSuccessCount());
        assertEquals(2, stats3.missCount());
        assertEquals(.666666, stats3.missRate(), EPSILON);
        assertEquals(3, stats3.requestCount());
        assertTrue(stats3.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats3.totalLoadTime();

        // Get another id - should be loaded.  This is the 3rd id in the cache and I've set the 
        // max size to 2 so the first id that was loaded should now be evicted.
        ApplicationPlatform appPlat31 = ApplicationPlatforms.Instance.getInstance().get(appPlat3.getId());
        assertEquals(appPlat3.getId(), appPlat31.getId());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map4 = cache.asMap();
        assertEquals(2, map4.size());
        assertTrue(map4.containsKey(appPlat2.getId()));
        assertTrue(map4.containsKey(appPlat3.getId()));

        CacheStats stats4 = cache.stats();
        assertEquals(1, stats4.evictionCount());
        assertEquals(1, stats4.hitCount());
        assertEquals(.25, stats4.hitRate(), EPSILON);
        assertEquals(3, stats4.loadCount());
        assertEquals(0, stats4.loadExceptionCount());
        assertEquals(0, stats4.loadExceptionRate(), EPSILON);
        assertEquals(3, stats4.loadSuccessCount());
        assertEquals(3, stats4.missCount());
        assertEquals(.75, stats4.missRate(), EPSILON);
        assertEquals(4, stats4.requestCount());
        assertTrue(stats4.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats4.totalLoadTime();

        // Get another id - should be loaded.  This is the 4th id in the cache and I've set the 
        // max size to 2 so the first and second ids that were loaded should now be evicted.
        ApplicationPlatform appPlat41 = ApplicationPlatforms.Instance.getInstance().get(appPlat4.getId());
        assertEquals(appPlat4.getId(), appPlat41.getId());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map5 = cache.asMap();
        assertEquals(2, map5.size());
        assertTrue(map5.containsKey(appPlat3.getId()));
        assertTrue(map5.containsKey(appPlat4.getId()));

        CacheStats stats5 = cache.stats();
        assertEquals(2, stats5.evictionCount());
        assertEquals(1, stats5.hitCount());
        assertEquals(.20, stats5.hitRate(), EPSILON);
        assertEquals(4, stats5.loadCount());
        assertEquals(0, stats5.loadExceptionCount());
        assertEquals(0, stats5.loadExceptionRate(), EPSILON);
        assertEquals(4, stats5.loadSuccessCount());
        assertEquals(4, stats5.missCount());
        assertEquals(.80, stats5.missRate(), EPSILON);
        assertEquals(5, stats5.requestCount());
        assertTrue(stats5.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats5.totalLoadTime();

        ApplicationPlatform appPlat42 = ApplicationPlatforms.Instance.getInstance().get(appPlat4.getId());
        assertEquals(appPlat4.getId(), appPlat42.getId());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map6 = cache.asMap();
        assertEquals(2, map6.size());
        assertTrue(map6.containsKey(appPlat3.getId()));
        assertTrue(map6.containsKey(appPlat4.getId()));

        CacheStats stats6 = cache.stats();
        assertEquals(2, stats6.evictionCount());
        assertEquals(2, stats6.hitCount());
        assertEquals(.333333, stats6.hitRate(), EPSILON);
        assertEquals(4, stats6.loadCount());
        assertEquals(0, stats6.loadExceptionCount());
        assertEquals(0, stats6.loadExceptionRate(), EPSILON);
        assertEquals(4, stats6.loadSuccessCount());
        assertEquals(4, stats6.missCount());
        assertEquals(.666666, stats6.missRate(), EPSILON);
        assertEquals(6, stats6.requestCount());
        assertEquals(lastLoadTime, stats6.totalLoadTime());
    }

    @Test
    public void testLoadAllValidIds() throws Exception
    {
        validateCacheInitialState(cache);

        ImmutableMap<Integer, ApplicationPlatform> appPlats1And21 = ApplicationPlatforms.Instance.getInstance().getAll(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));
        assertTrue(appPlats1And21.containsKey(appPlat1.getId()));
        assertTrue(appPlats1And21.containsKey(appPlat2.getId()));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map1 = cache.asMap();
        assertEquals(2, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));
        assertTrue(map1.containsKey(appPlat2.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(0, stats1.hitCount());
        assertEquals(0, stats1.hitRate(), EPSILON);
        assertEquals(1, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(1, stats1.loadSuccessCount());
        assertEquals(2, stats1.missCount());
        assertEquals(1.0, stats1.missRate(), EPSILON);
        assertEquals(2, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        // Get the same ids - should be returned from the cache instead of loaded.
        ImmutableMap<Integer, ApplicationPlatform> appPlats1And22 = ApplicationPlatforms.Instance.getInstance().getAll(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));
        assertTrue(appPlats1And22.containsKey(appPlat1.getId()));
        assertTrue(appPlats1And22.containsKey(appPlat2.getId()));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map2 = cache.asMap();
        assertEquals(2, map2.size());
        assertTrue(map2.containsKey(appPlat1.getId()));
        assertTrue(map2.containsKey(appPlat2.getId()));

        CacheStats stats2 = cache.stats();
        assertEquals(0, stats2.evictionCount());
        assertEquals(2, stats2.hitCount());
        assertEquals(.5, stats2.hitRate(), EPSILON);
        assertEquals(1, stats2.loadCount());
        assertEquals(0, stats2.loadExceptionCount());
        assertEquals(0, stats2.loadExceptionRate(), EPSILON);
        assertEquals(1, stats2.loadSuccessCount());
        assertEquals(2, stats2.missCount());
        assertEquals(.5, stats2.missRate(), EPSILON);
        assertEquals(4, stats2.requestCount());
        assertEquals(lastLoadTime, stats2.totalLoadTime());

        // Get more ids - mix of one hit and one miss which should be loaded.
        ImmutableMap<Integer, ApplicationPlatform> appPlatsId2And31 = ApplicationPlatforms.Instance.getInstance()
                .getAll(
                Arrays.asList(new Integer[] { appPlat2.getId(), appPlat3.getId() }));
        assertTrue(appPlatsId2And31.containsKey(appPlat2.getId()));
        assertTrue(appPlatsId2And31.containsKey(appPlat3.getId()));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertTrue(map3.containsKey(appPlat2.getId()));
        assertTrue(map3.containsKey(appPlat3.getId()));

        CacheStats stats3 = cache.stats();
        assertEquals(1, stats3.evictionCount());
        assertEquals(3, stats3.hitCount());
        assertEquals(.5, stats3.hitRate(), EPSILON);
        assertEquals(2, stats3.loadCount());
        assertEquals(0, stats3.loadExceptionCount());
        assertEquals(0, stats3.loadExceptionRate(), EPSILON);
        assertEquals(2, stats3.loadSuccessCount());
        assertEquals(3, stats3.missCount());
        assertEquals(.5, stats3.missRate(), EPSILON);
        assertEquals(6, stats3.requestCount());
        assertTrue(stats3.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats3.totalLoadTime();

        // Get more ids - one should be in the cache and one should be loaded.
        ImmutableMap<Integer, ApplicationPlatform> appPlatsId3And41 = ApplicationPlatforms.Instance.getInstance()
                .getAll(
                Arrays.asList(new Integer[] { appPlat3.getId(), appPlat4.getId() }));
        assertTrue(appPlatsId3And41.containsKey(appPlat3.getId()));
        assertTrue(appPlatsId3And41.containsKey(appPlat4.getId()));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map4 = cache.asMap();
        assertEquals(2, map4.size());
        assertTrue(map4.containsKey(appPlat3.getId()));
        assertTrue(map4.containsKey(appPlat4.getId()));

        CacheStats stats4 = cache.stats();
        assertEquals(2, stats4.evictionCount());
        assertEquals(4, stats4.hitCount());
        assertEquals(.5, stats4.hitRate(), EPSILON);
        assertEquals(3, stats4.loadCount());
        assertEquals(0, stats4.loadExceptionCount());
        assertEquals(0, stats4.loadExceptionRate(), EPSILON);
        assertEquals(3, stats4.loadSuccessCount());
        assertEquals(4, stats4.missCount());
        assertEquals(.5, stats4.missRate(), EPSILON);
        assertEquals(8, stats4.requestCount());
        assertTrue(stats4.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats4.totalLoadTime();

        ImmutableMap<Integer, ApplicationPlatform> appPlatsId3And42 = ApplicationPlatforms.Instance.getInstance()
                .getAll(
                Arrays.asList(new Integer[] { appPlat3.getId(), appPlat4.getId() }));
        assertTrue(appPlatsId3And42.containsKey(appPlat3.getId()));
        assertTrue(appPlatsId3And42.containsKey(appPlat4.getId()));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map5 = cache.asMap();
        assertEquals(2, map5.size());
        assertTrue(map5.containsKey(appPlat3.getId()));
        assertTrue(map5.containsKey(appPlat4.getId()));

        CacheStats stats5 = cache.stats();
        assertEquals(2, stats5.evictionCount());
        assertEquals(6, stats5.hitCount());
        assertEquals(.6, stats5.hitRate(), EPSILON);
        assertEquals(3, stats5.loadCount());
        assertEquals(0, stats5.loadExceptionCount());
        assertEquals(0, stats5.loadExceptionRate(), EPSILON);
        assertEquals(3, stats5.loadSuccessCount());
        assertEquals(4, stats5.missCount());
        assertEquals(.4, stats5.missRate(), EPSILON);
        assertEquals(10, stats5.requestCount());
        assertEquals(lastLoadTime, stats5.totalLoadTime());
    }

    @Test
    public void testInitialize() throws Exception
    {
        validateCacheInitialState(cache);

        ApplicationPlatform appPlat11 = ApplicationPlatforms.Instance.getInstance().get(appPlat1.getId());
        assertEquals(appPlat1.getId(), appPlat11.getId());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map1 = cache.asMap();
        assertEquals(1, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(0, stats1.hitCount());
        assertEquals(0, stats1.hitRate(), EPSILON);
        assertEquals(1, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(1, stats1.loadSuccessCount());
        assertEquals(1, stats1.missCount());
        assertEquals(1.0, stats1.missRate(), EPSILON);
        assertEquals(1, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        CacheInitializationParameters cip = new CacheInitializationParameters(2, true);
        ApplicationPlatforms.Instance.getInstance().initialize(cip);
        createCacheReference();

        assertEquals(0, cache.size());

        ConcurrentMap<Integer, ApplicationPlatform> map2 = cache.asMap();
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

