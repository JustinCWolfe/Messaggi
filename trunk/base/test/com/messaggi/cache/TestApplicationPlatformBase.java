package com.messaggi.cache;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
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

public class TestApplicationPlatformBase extends MessaggiTestCase
{
    protected static final double EPSILON = 1e-5;

    protected static Application app1;

    protected static Application app2;

    protected static ApplicationPlatform appPlat1;

    protected static ApplicationPlatform appPlat2;

    protected static ApplicationPlatform appPlat3;

    protected static ApplicationPlatform appPlat4;

    protected static User user1;

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
    
    @SuppressWarnings("unchecked")
    protected static Field getCacheField(Class<?> cacheImplClass) throws Exception
    {
        Field cacheField = cacheImplClass.getDeclaredField("cache");
        cacheField.setAccessible(true);
        return cacheField;
    }
    
    protected static void validateCacheInitialState(LoadingCache<?, ?> cache)
    {
        assertEquals(0, cache.size());

        ConcurrentMap<?, ?> map = cache.asMap();
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
}

