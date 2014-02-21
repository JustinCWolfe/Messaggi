package com.messaggi.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.ApplicationPlatform4;

public class TestApplicationPlatformTokens extends ApplicationPlatformCacheTestCase
{
    private LoadingCache<UUID, Integer> cache;

    @Override
    @Before
    public void setUp() throws Exception
    {
        CacheInitializationParameters cip = new CacheInitializationParameters(2, true);
        ApplicationPlatformTokens.Instance.getInstance().initialize(cip);
        createCacheReference();
    }

    @SuppressWarnings("unchecked")
    private void createCacheReference() throws Exception
    {
        Field cacheField = getCacheField(ApplicationPlatformTokensImpl.class);
        cache = (LoadingCache<UUID, Integer>) cacheField.get(ApplicationPlatformTokens.Instance.getInstance());
    }

    @Test
    public void testLoadInvalidToken() throws Exception
    {
        validateCacheInitialState(cache);

        UUID nonExistentUUID = UUID.randomUUID();
        try {
            ApplicationPlatformTokens.Instance.getInstance().get(nonExistentUUID);
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<UUID, Integer> map = cache.asMap();
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
    public void testLoadAllInvalidTokens() throws Exception
    {
        validateCacheInitialState(cache);

        UUID nonExistentUUID1 = UUID.randomUUID();
        UUID nonExistentUUID2 = UUID.randomUUID();
        try {
            ApplicationPlatformTokens.Instance.getInstance().getAll(
                    Arrays.asList(new UUID[] { nonExistentUUID1, nonExistentUUID2 }));
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<UUID, Integer> map = cache.asMap();
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
    public void testLoadValidToken() throws Exception
    {
        validateCacheInitialState(cache);

        Integer appPlat11 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform1.TOKEN);
        assertEquals(appPlat1.getId(), appPlat11);

        assertEquals(1, cache.size());

        ConcurrentMap<UUID, Integer> map1 = cache.asMap();
        assertEquals(1, map1.size());
        assertEquals(appPlat1.getId(), map1.get(ApplicationPlatform1.TOKEN));

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

        // Get the same token - should be returned from the cache instead of loaded.
        Integer appPlat12 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform1.TOKEN);
        assertEquals(appPlat1.getId(), appPlat12);

        assertEquals(1, cache.size());

        ConcurrentMap<UUID, Integer> map2 = cache.asMap();
        assertEquals(1, map2.size());
        assertEquals(appPlat1.getId(), map2.get(ApplicationPlatform1.TOKEN));

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

        // Get another token - should be loaded.
        Integer appPlat21 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform2.TOKEN);
        assertEquals(appPlat2.getId(), appPlat21);

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertEquals(appPlat1.getId(), map3.get(ApplicationPlatform1.TOKEN));
        assertEquals(appPlat2.getId(), map3.get(ApplicationPlatform2.TOKEN));

        CacheStats stats3 = cache.stats();
        assertEquals(0, stats3.evictionCount());
        assertEquals(1, stats3.hitCount());
        assertEquals(.3333333333333333, stats3.hitRate(), EPSILON);
        assertEquals(2, stats3.loadCount());
        assertEquals(0, stats3.loadExceptionCount());
        assertEquals(0, stats3.loadExceptionRate(), EPSILON);
        assertEquals(2, stats3.loadSuccessCount());
        assertEquals(2, stats3.missCount());
        assertEquals(.6666666666666666, stats3.missRate(), EPSILON);
        assertEquals(3, stats3.requestCount());
        assertTrue(stats3.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats3.totalLoadTime();

        // Get another token - should be loaded.  This is the 3rd token in the cache and I've set the 
        // max size to 2 so the first token that was loaded should now be evicted.
        Integer appPlat31 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform3.TOKEN);
        assertEquals(appPlat3.getId(), appPlat31);

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map4 = cache.asMap();
        assertEquals(2, map4.size());
        assertEquals(appPlat2.getId(), map4.get(ApplicationPlatform2.TOKEN));
        assertEquals(appPlat3.getId(), map4.get(ApplicationPlatform3.TOKEN));

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

        // Get another token - should be loaded.  This is the 4th token in the cache and I've set the 
        // max size to 2 so the first and second tokens that were loaded should now be evicted.
        Integer appPlat41 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform4.TOKEN);
        assertEquals(appPlat4.getId(), appPlat41);

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map5 = cache.asMap();
        assertEquals(2, map5.size());
        assertEquals(appPlat3.getId(), map5.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), map5.get(ApplicationPlatform4.TOKEN));

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

        Integer appPlat42 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform4.TOKEN);
        assertEquals(appPlat4.getId(), appPlat42);

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map6 = cache.asMap();
        assertEquals(2, map6.size());
        assertEquals(appPlat3.getId(), map6.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), map6.get(ApplicationPlatform4.TOKEN));

        CacheStats stats6 = cache.stats();
        assertEquals(2, stats6.evictionCount());
        assertEquals(2, stats6.hitCount());
        assertEquals(.3333333333333333, stats6.hitRate(), EPSILON);
        assertEquals(4, stats6.loadCount());
        assertEquals(0, stats6.loadExceptionCount());
        assertEquals(0, stats6.loadExceptionRate(), EPSILON);
        assertEquals(4, stats6.loadSuccessCount());
        assertEquals(4, stats6.missCount());
        assertEquals(.6666666666666666, stats6.missRate(), EPSILON);
        assertEquals(6, stats6.requestCount());
        assertEquals(lastLoadTime, stats6.totalLoadTime());
    }

    @Test
    public void testLoadAllValidTokens() throws Exception
    {
        validateCacheInitialState(cache);

        ImmutableMap<UUID, Integer> appPlatsId1 = ApplicationPlatformTokens.Instance.getInstance().getAll(
                Arrays.asList(new UUID[] { ApplicationPlatform1.TOKEN, ApplicationPlatform2.TOKEN }));
        assertEquals(appPlat1.getId(), appPlatsId1.get(ApplicationPlatform1.TOKEN));
        assertEquals(appPlat2.getId(), appPlatsId1.get(ApplicationPlatform2.TOKEN));

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map1 = cache.asMap();
        assertEquals(2, map1.size());
        assertEquals(appPlat1.getId(), map1.get(ApplicationPlatform1.TOKEN));
        assertEquals(appPlat2.getId(), map1.get(ApplicationPlatform2.TOKEN));

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

        // Get the same tokens - should be returned from the cache instead of loaded.
        ImmutableMap<UUID, Integer> appPlatsId2 = ApplicationPlatformTokens.Instance.getInstance().getAll(
                Arrays.asList(new UUID[] { ApplicationPlatform1.TOKEN, ApplicationPlatform2.TOKEN }));
        assertEquals(appPlat1.getId(), appPlatsId2.get(ApplicationPlatform1.TOKEN));
        assertEquals(appPlat2.getId(), appPlatsId2.get(ApplicationPlatform2.TOKEN));

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map2 = cache.asMap();
        assertEquals(2, map2.size());
        assertEquals(appPlat1.getId(), map2.get(ApplicationPlatform1.TOKEN));
        assertEquals(appPlat2.getId(), map2.get(ApplicationPlatform2.TOKEN));

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

        // Get more tokens - mix of one hit and one miss which should be loaded.
        ImmutableMap<UUID, Integer> appPlatsId3 = ApplicationPlatformTokens.Instance.getInstance().getAll(
                Arrays.asList(new UUID[] { ApplicationPlatform2.TOKEN, ApplicationPlatform3.TOKEN }));
        assertEquals(appPlat2.getId(), appPlatsId3.get(ApplicationPlatform2.TOKEN));
        assertEquals(appPlat3.getId(), appPlatsId3.get(ApplicationPlatform3.TOKEN));

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertEquals(appPlat2.getId(), map3.get(ApplicationPlatform2.TOKEN));
        assertEquals(appPlat3.getId(), map3.get(ApplicationPlatform3.TOKEN));

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

        // Get more tokens - one should be in the cache and one should be loaded.
        ImmutableMap<UUID, Integer> appPlatsId4 = ApplicationPlatformTokens.Instance.getInstance().getAll(
                Arrays.asList(new UUID[] { ApplicationPlatform3.TOKEN, ApplicationPlatform4.TOKEN }));
        assertEquals(appPlat3.getId(), appPlatsId4.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), appPlatsId4.get(ApplicationPlatform4.TOKEN));

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map4 = cache.asMap();
        assertEquals(2, map4.size());
        assertEquals(appPlat3.getId(), map4.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), map4.get(ApplicationPlatform4.TOKEN));

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

        ImmutableMap<UUID, Integer> appPlatsId5 = ApplicationPlatformTokens.Instance.getInstance().getAll(
                Arrays.asList(new UUID[] { ApplicationPlatform3.TOKEN, ApplicationPlatform4.TOKEN }));
        assertEquals(appPlat3.getId(), appPlatsId5.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), appPlatsId5.get(ApplicationPlatform4.TOKEN));

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map5 = cache.asMap();
        assertEquals(2, map5.size());
        assertEquals(appPlat3.getId(), map5.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), map5.get(ApplicationPlatform4.TOKEN));

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

        Integer appPlat11 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform1.TOKEN);
        assertEquals(appPlat1.getId(), appPlat11);

        assertEquals(1, cache.size());

        ConcurrentMap<UUID, Integer> map1 = cache.asMap();
        assertEquals(1, map1.size());
        assertEquals(appPlat1.getId(), map1.get(ApplicationPlatform1.TOKEN));

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
        ApplicationPlatformTokens.Instance.getInstance().initialize(cip);
        createCacheReference();

        assertEquals(0, cache.size());

        ConcurrentMap<UUID, Integer> map2 = cache.asMap();
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

