package com.messaggi.cache;

import static org.junit.Assert.assertEquals;
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

    private static LoadingCache<UUID, Integer> cache;

    private static Application app1;

    private static Application app2;

    private static ApplicationPlatform appPlat1;

    private static ApplicationPlatform appPlat2;

    private static ApplicationPlatform appPlat3;

    private static ApplicationPlatform appPlat4;

    private static User user1;

    @SuppressWarnings("unchecked")
    private static void createCacheReference() throws Exception
    {
        Field cacheField = ApplicationPlatformTokensImpl.class.getDeclaredField("applicationPlatformTokenCache");
        cacheField.setAccessible(true);
        cache = (LoadingCache<UUID, Integer>) cacheField.get(ApplicationPlatformTokens.Instance.getInstance());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        createCacheReference();
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
        CacheInitializationParameters cip = new CacheInitializationParameters(1000, true);
        ApplicationPlatformTokens.Instance.getInstance().initialize(cip);
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
    }

    private static ConcurrentMap<UUID, Integer> getCacheMap()
    {
        return cache.asMap();
    }

    private static long getCacheSize()
    {
        return cache.size();
    }

    private static CacheStats getCacheStats()
    {
        return cache.stats();
    }

    private void validateCacheInitialState()
    {
        assertEquals(0, getCacheSize());

        ConcurrentMap<UUID, Integer> map = getCacheMap();
        assertEquals(0, map.size());

        CacheStats stats = getCacheStats();
        assertEquals(0, stats.averageLoadPenalty(), EPSILON);
        assertEquals(0, stats.evictionCount());
        assertEquals(0, stats.hitCount());
        assertEquals(1.0, stats.hitRate(), EPSILON);
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
    public void testInvalidToken() throws Exception
    {
        validateCacheInitialState();
        UUID nonExistentUUID = UUID.randomUUID();
        try {
            ApplicationPlatformTokens.Instance.getInstance().get(nonExistentUUID);
            fail("The call above should have thrown");
        } catch (UncheckedExecutionException e) {
            validateCacheInitialState();
            return;
        }
        fail("The call above should have thrown and UncheckedExecutionException");
    }

    @Test
    public void testValidToken() throws Exception
    {
        validateCacheInitialState();
        Integer appPlat1Id = ApplicationPlatformTokens.Instance.getInstance().get(ApplicationPlatform1.TOKEN);
        assertEquals(appPlat1.getId(), appPlat1Id);

        assertEquals(1, getCacheSize());

        ConcurrentMap<UUID, Integer> map = getCacheMap();
        assertEquals(1, map.size());
        assertEquals(appPlat1.getId(), map.get(ApplicationPlatform1.TOKEN));

        CacheStats stats = getCacheStats();
        assertEquals(0, stats.averageLoadPenalty(), EPSILON);
        assertEquals(0, stats.evictionCount());
        assertEquals(0, stats.hitCount());
        assertEquals(1.0, stats.hitRate(), EPSILON);
        assertEquals(0, stats.loadCount());
        assertEquals(0, stats.loadExceptionCount());
        assertEquals(0, stats.loadExceptionRate(), EPSILON);
        assertEquals(0, stats.loadSuccessCount());
        assertEquals(0, stats.missCount());
        assertEquals(0, stats.missRate(), EPSILON);
        assertEquals(0, stats.requestCount());
        assertEquals(0, stats.totalLoadTime());
    }
}

