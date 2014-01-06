package com.messaggi.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Application1;
import com.messaggi.TestDataHelper.Application2;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.ApplicationPlatform4;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestApplicationPlatformTokens extends MessaggiTestCase
{
    private static final double EPSILON = 1e-5;

    private static Application app1;

    private static Application app2;

    private static ApplicationPlatform appPlat1;

    private static ApplicationPlatform appPlat2;

    private static ApplicationPlatform appPlat3;

    private static ApplicationPlatform appPlat4;

    private static User user1;

    private LoadingCache<UUID, Integer> cache;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        user1 = User1.getDomainObject();
        TestDataHelper.createUser(user1);
        app1 = Application1.getDomainObject();
        app2 = Application2.getDomainObject();
        app1.setUser(user1);
        app2.setUser(user1);
        TestDataHelper.createApplication(app1);
        TestDataHelper.createApplication(app2);
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);
        TestDataHelper.createApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatform(appPlat4);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        TestDataHelper.deleteApplicationPlatform(appPlat1);
        TestDataHelper.deleteApplicationPlatform(appPlat2);
        TestDataHelper.deleteApplicationPlatform(appPlat3);
        TestDataHelper.deleteApplicationPlatform(appPlat4);
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteApplication(app2);
        TestDataHelper.deleteUser(user1);
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        CacheInitializationParameters cip = new CacheInitializationParameters(2, true);
        ApplicationPlatformTokens.Instance.getInstance().initialize(cip);
        createCacheReference();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        cache = null;
    }

    @SuppressWarnings("unchecked")
    private void createCacheReference() throws Exception
    {
        Field cacheField = ApplicationPlatformTokensImpl.class.getDeclaredField("applicationPlatformTokenCache");
        cacheField.setAccessible(true);
        cache = (LoadingCache<UUID, Integer>) cacheField.get(ApplicationPlatformTokens.Instance.getInstance());
    }

    private void validateCacheInitialState()
    {
        assertEquals(0, cache.size());

        ConcurrentMap<UUID, Integer> map = cache.asMap();
        assertEquals(0, map.size());

        CacheStats stats = cache.stats();
        assertEquals(0, stats.evictionCount());
        assertEquals(0, stats.hitCount());
        assertEquals(0, stats.loadCount());
        assertEquals(0, stats.loadExceptionCount());
        assertEquals(0, stats.loadExceptionRate(), EPSILON);
        assertEquals(0, stats.loadSuccessCount());
        assertEquals(0, stats.missCount());
        assertEquals(0, stats.missRate(), EPSILON);
        assertEquals(0, stats.requestCount());
        assertEquals(0, stats.totalLoadTime());
    }

    @Test
    public void testLoadInvalidToken() throws Exception
    {
        validateCacheInitialState();

        UUID nonExistentUUID = UUID.randomUUID();
        try {
            ApplicationPlatformTokens.Instance.getInstance().get(nonExistentUUID);
            fail("The call above should have thrown");
        } catch (UncheckedExecutionException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<UUID, Integer> map = cache.asMap();
            assertEquals(0, map.size());

            long lastLoadTime = 0;
            CacheStats stats = cache.stats();
            assertEquals(0, stats.evictionCount());
            assertEquals(0, stats.hitCount());
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
        fail("The call above should have thrown and UncheckedExecutionException");
    }

    @Test
    public void testLoadAllInvalidTokens() throws Exception
    {

    }

    @Test
    public void testLoadValidToken() throws Exception
    {
        validateCacheInitialState();

        Integer appPlat1Id1 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform1.TOKEN);
        assertEquals(appPlat1.getId(), appPlat1Id1);

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
        Integer appPlat1Id2 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform1.TOKEN);
        assertEquals(appPlat1.getId(), appPlat1Id2);

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
        Integer appPlat1Id3 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform2.TOKEN);
        assertEquals(appPlat2.getId(), appPlat1Id3);

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertEquals(appPlat1.getId(), map3.get(ApplicationPlatform1.TOKEN));
        assertEquals(appPlat2.getId(), map3.get(ApplicationPlatform2.TOKEN));

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

        // Get another token - should be loaded.  This is the 3rd token in the cache and I've set the 
        // max size to 2 so the first token that was loaded should now be evicted.
        Integer appPlat1Id4 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform3.TOKEN);
        assertEquals(appPlat3.getId(), appPlat1Id4);

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
        Integer appPlat1Id5 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform4.TOKEN);
        assertEquals(appPlat4.getId(), appPlat1Id5);

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

        Integer appPlat1Id6 = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform4.TOKEN);
        assertEquals(appPlat4.getId(), appPlat1Id6);

        assertEquals(2, cache.size());

        ConcurrentMap<UUID, Integer> map6 = cache.asMap();
        assertEquals(2, map6.size());
        assertEquals(appPlat3.getId(), map6.get(ApplicationPlatform3.TOKEN));
        assertEquals(appPlat4.getId(), map6.get(ApplicationPlatform4.TOKEN));

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
    public void testLoadAllValidTokens() throws Exception
    {

    }
}

