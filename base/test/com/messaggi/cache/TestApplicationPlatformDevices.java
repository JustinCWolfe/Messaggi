package com.messaggi.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Device1;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.Device3;
import com.messaggi.TestDataHelper.Device4;
import com.messaggi.TestDataHelper.Device5;
import com.messaggi.TestDataHelper.Device6;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class TestApplicationPlatformDevices extends TestApplicationPlatformBase
{
    private static Device d1;

    private static Device d2;

    private static Device d3;

    private static Device d4;

    private static Device d5;

    private static Device d6;

    private LoadingCache<Integer, LoadingCache<String, Device>> cache;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        TestApplicationPlatformBase.setUpBeforeClass();
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        ApplicationPlatform[] appPlats2 = { appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat3 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        TestDataHelper.createDevice(d1);
        TestDataHelper.createDevice(d2);
        TestDataHelper.createDevice(d3);
        TestDataHelper.createDevice(d4);
        TestDataHelper.createDevice(d5);
        TestDataHelper.createDevice(d6);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        TestDataHelper.deleteDevice(d1);
        TestDataHelper.deleteDevice(d2);
        TestDataHelper.deleteDevice(d3);
        TestDataHelper.deleteDevice(d4);
        TestDataHelper.deleteDevice(d5);
        TestDataHelper.deleteDevice(d6);
        TestApplicationPlatformBase.tearDownAfterClass();
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        CacheInitializationParameters cip = new CacheInitializationParameters(2, true);
        ApplicationPlatformDevices.Instance.getInstance().initialize(cip);
        createCacheReference();
    }

    @SuppressWarnings("unchecked")
    private void createCacheReference() throws Exception
    {
        Field cacheField = getCacheField(ApplicationPlatformDevicesImpl.class);
        cache = (LoadingCache<Integer, LoadingCache<String, Device>>) cacheField
                .get(ApplicationPlatformDevices.Instance.getInstance());
    }

    private LoadingCache<String, Device> getDeviceCacheReferenceForApplicationPlatform(Integer id) throws Exception
    {
        return cache.get(id);
    }

    @Test
    public void testLoadInvalidIdAndCode() throws Exception
    {
        validateCacheInitialState(cache);

        int nonExistentID = -1;
        String nonExistentCode = "some device code that does not exists - " + UUID.randomUUID().toString();
        try {
            ApplicationPlatformDevices.Instance.getInstance().getDevice(nonExistentID, nonExistentCode);
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<Integer, LoadingCache<String, Device>> map = cache.asMap();
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
    public void testLoadInvalidIdValidCode() throws Exception
    {
        validateCacheInitialState(cache);

        int nonExistentID = -1;
        try {
            ApplicationPlatformDevices.Instance.getInstance().getDevice(nonExistentID, d1.getCode());
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(0, cache.size());

            ConcurrentMap<Integer, LoadingCache<String, Device>> map = cache.asMap();
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
    public void testLoadValidIdInvalidCode() throws Exception
    {
        validateCacheInitialState(cache);

        String nonExistentCode = "some device code that does not exists - " + UUID.randomUUID().toString();
        try {
            ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), nonExistentCode);
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<String, Device>> map = cache.asMap();
            assertEquals(1, map.size());
            assertTrue(map.containsKey(appPlat1.getId()));

            long lastLoadTime = 0;
            CacheStats stats = cache.stats();
            assertEquals(0, stats.evictionCount());
            assertEquals(0, stats.hitCount());
            assertEquals(0.0, stats.hitRate(), EPSILON);
            assertEquals(1, stats.loadCount());
            assertEquals(0, stats.loadExceptionCount());
            assertEquals(0.0, stats.loadExceptionRate(), EPSILON);
            assertEquals(1, stats.loadSuccessCount());
            assertEquals(1, stats.missCount());
            assertEquals(1.0, stats.missRate(), EPSILON);
            assertEquals(1, stats.requestCount());
            assertTrue(stats.totalLoadTime() > lastLoadTime);

            LoadingCache<String, Device> deviceCache = getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId());
            assertEquals(0, deviceCache.size());

            ConcurrentMap<String, Device> deviceMap = deviceCache.asMap();
            assertEquals(0, deviceMap.size());

            long deviceLastLoadTime = 0;
            CacheStats deviceStats = deviceCache.stats();
            assertEquals(0, deviceStats.evictionCount());
            assertEquals(0, deviceStats.hitCount());
            assertEquals(0.0, deviceStats.hitRate(), EPSILON);
            assertEquals(1, deviceStats.loadCount());
            assertEquals(1, deviceStats.loadExceptionCount());
            assertEquals(1.0, deviceStats.loadExceptionRate(), EPSILON);
            assertEquals(0, deviceStats.loadSuccessCount());
            assertEquals(1, deviceStats.missCount());
            assertEquals(1.0, deviceStats.missRate(), EPSILON);
            assertEquals(1, deviceStats.requestCount());
            assertTrue(deviceStats.totalLoadTime() > deviceLastLoadTime);

            return;
        }
        fail("The call above should have thrown and InvalidCacheLoadException");
    }

    @Test
    public void testLoadValidId() throws Exception
    {
        validateCacheInitialState(cache);

        Device d11 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
        Device d21 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d2.getCode());
        Device d31 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d3.getCode());
        Device d41 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
        assertEquals(d1.getCode(), d11.getCode());
        assertEquals(d2.getCode(), d21.getCode());
        assertNull(d31);
          
        assertEquals(1, cache.size());
          
        ConcurrentMap<Integer, LoadingCache<String, Device>> map1 = cache.asMap();
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

        LoadingCache<String, Device> deviceCache1 = getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId());
        assertEquals(2, deviceCache1.size());

        ConcurrentMap<String, Device> deviceMap1 = deviceCache1.asMap();
        assertEquals(1, deviceMap1.size());
        assertTrue(deviceMap1.containsKey(d1.getCode()));
        assertTrue(deviceMap1.containsKey(d2.getCode()));

        long deviceLastLoadTime = 0;
        CacheStats deviceStats1 = deviceCache1.stats();
        assertEquals(0, deviceStats1.evictionCount());
        assertEquals(1, deviceStats1.hitCount());
        assertEquals(.25, deviceStats1.hitRate(), EPSILON);
        assertEquals(2, deviceStats1.loadCount());
        assertEquals(1, deviceStats1.loadExceptionCount());
        assertEquals(.25, deviceStats1.loadExceptionRate(), EPSILON);
        assertEquals(2, deviceStats1.loadSuccessCount());
        assertEquals(2, deviceStats1.missCount());
        assertEquals(.75, deviceStats1.missRate(), EPSILON);
        assertEquals(4, deviceStats1.requestCount());
        assertTrue(deviceStats1.totalLoadTime() > deviceLastLoadTime);
    }

    @Test
    public void testCreateDeviceCacheForAllApplicationPlatforms() throws Exception
    {
        ApplicationPlatformDevices.Instance.getInstance().createDeviceCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<String, Device>> map1 = cache.asMap();
        assertEquals(2, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));
        assertTrue(map1.containsKey(appPlat2.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(0, stats1.hitCount());
        assertEquals(0.0, stats1.hitRate(), EPSILON);
        assertEquals(1, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0.0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(1, stats1.loadSuccessCount());
        assertEquals(2, stats1.missCount());
        assertEquals(1.0, stats1.missRate(), EPSILON);
        assertEquals(2, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        List<LoadingCache<String, Device>> deviceCaches1 = new ArrayList<>();
        deviceCaches1.add(getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId()));
        deviceCaches1.add(getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId()));
        for (LoadingCache<String, Device> deviceCache : deviceCaches1) {
            assertEquals(0, deviceCache.size());

            ConcurrentMap<String, Device> deviceMap = deviceCache.asMap();
            assertEquals(0, deviceMap.size());

            long deviceLastLoadTime = 0;
            CacheStats deviceStats = deviceCache.stats();
            assertEquals(0, deviceStats.evictionCount());
            assertEquals(0, deviceStats.hitCount());
            // Hit rate is 1 when request count is 0.
            assertEquals(1.0, deviceStats.hitRate(), EPSILON);
            assertEquals(0, deviceStats.loadCount());
            assertEquals(0, deviceStats.loadExceptionCount());
            assertEquals(0.0, deviceStats.loadExceptionRate(), EPSILON);
            assertEquals(0, deviceStats.loadSuccessCount());
            assertEquals(0, deviceStats.missCount());
            assertEquals(0.0, deviceStats.missRate(), EPSILON);
            assertEquals(0, deviceStats.requestCount());
            assertEquals(deviceStats.totalLoadTime(), deviceLastLoadTime);
        }

        ApplicationPlatformDevices.Instance.getInstance().createDeviceCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<String, Device>> map2 = cache.asMap();
        assertEquals(2, map2.size());
        assertTrue(map2.containsKey(appPlat1.getId()));
        assertTrue(map2.containsKey(appPlat2.getId()));

        CacheStats stats2 = cache.stats();
        assertEquals(0, stats2.evictionCount());
        // Hit count is 4 instead of 2 because validating the device cache contents also hits the cache.
        assertEquals(2 + deviceCaches1.size(), stats2.hitCount());
        assertEquals(.66666, stats2.hitRate(), EPSILON);
        assertEquals(1, stats2.loadCount());
        assertEquals(0, stats2.loadExceptionCount());
        assertEquals(0.0, stats2.loadExceptionRate(), EPSILON);
        assertEquals(1, stats2.loadSuccessCount());
        assertEquals(2, stats2.missCount());
        assertEquals(.33333, stats2.missRate(), EPSILON);
        // Request count is 6 instead of 4 because validating the device cache contents also hits the cache.
        assertEquals(4 + deviceCaches1.size(), stats2.requestCount());
        assertEquals(stats2.totalLoadTime(), lastLoadTime);
        
        List<LoadingCache<String, Device>> deviceCaches2 = new ArrayList<>();
        deviceCaches2.add(getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId()));
        deviceCaches2.add(getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId()));
        for (LoadingCache<String, Device> deviceCache : deviceCaches2) {
	        assertEquals(0, deviceCache.size());
	
	        ConcurrentMap<String, Device> deviceMap = deviceCache.asMap();
	        assertEquals(0, deviceMap.size());
	
            long deviceLastLoadTime = 0;
	        CacheStats deviceStats = deviceCache.stats();
	        assertEquals(0, deviceStats.evictionCount());
	        assertEquals(0, deviceStats.hitCount());
            // Hit rate is 1 when request count is 0.
            assertEquals(1.0, deviceStats.hitRate(), EPSILON);
            assertEquals(0, deviceStats.loadCount());
            assertEquals(0, deviceStats.loadExceptionCount());
            assertEquals(0.0, deviceStats.loadExceptionRate(), EPSILON);
	        assertEquals(0, deviceStats.loadSuccessCount());
            assertEquals(0, deviceStats.missCount());
            assertEquals(0.0, deviceStats.missRate(), EPSILON);
            assertEquals(0, deviceStats.requestCount());
            assertEquals(deviceStats.totalLoadTime(), deviceLastLoadTime);
        }

        ApplicationPlatformDevices.Instance.getInstance().createDeviceCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat3.getId(), appPlat4.getId() }));

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<String, Device>> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertTrue(map3.containsKey(appPlat3.getId()));
        assertTrue(map3.containsKey(appPlat4.getId()));

        CacheStats stats3 = cache.stats();
        assertEquals(2, stats3.evictionCount());
        // Hit count is 6 instead of 2 because validating the device cache contents also hits the cache.
        assertEquals(2 + deviceCaches1.size() + deviceCaches2.size(), stats3.hitCount());
        assertEquals(.6, stats3.hitRate(), EPSILON);
        assertEquals(2, stats3.loadCount());
        assertEquals(0, stats3.loadExceptionCount());
        assertEquals(0.0, stats3.loadExceptionRate(), EPSILON);
        assertEquals(2, stats3.loadSuccessCount());
        assertEquals(4, stats3.missCount());
        assertEquals(.4, stats3.missRate(), EPSILON);
        // Request count is 10 instead of 6 because validating the device cache contents also hits the cache.
        assertEquals(6 + deviceCaches1.size() + deviceCaches2.size(), stats3.requestCount());
        assertTrue(stats3.totalLoadTime() > lastLoadTime);

        List<LoadingCache<String, Device>> deviceCaches3 = new ArrayList<>();
        deviceCaches3.add(getDeviceCacheReferenceForApplicationPlatform(appPlat3.getId()));
        deviceCaches3.add(getDeviceCacheReferenceForApplicationPlatform(appPlat4.getId()));
        for (LoadingCache<String, Device> deviceCache : deviceCaches2) {
            assertEquals(0, deviceCache.size());

            ConcurrentMap<String, Device> deviceMap = deviceCache.asMap();
            assertEquals(0, deviceMap.size());

            long deviceLastLoadTime = 0;
            CacheStats deviceStats = deviceCache.stats();
            assertEquals(0, deviceStats.evictionCount());
            assertEquals(0, deviceStats.hitCount());
            // Hit rate is 1 when request count is 0.
            assertEquals(1.0, deviceStats.hitRate(), EPSILON);
            assertEquals(0, deviceStats.loadCount());
            assertEquals(0, deviceStats.loadExceptionCount());
            assertEquals(0.0, deviceStats.loadExceptionRate(), EPSILON);
            assertEquals(0, deviceStats.loadSuccessCount());
            assertEquals(0, deviceStats.missCount());
            assertEquals(0.0, deviceStats.missRate(), EPSILON);
            assertEquals(0, deviceStats.requestCount());
            assertEquals(deviceStats.totalLoadTime(), deviceLastLoadTime);
        }
    }
}

