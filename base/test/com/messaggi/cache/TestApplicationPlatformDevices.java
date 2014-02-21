package com.messaggi.cache;

import static org.junit.Assert.assertEquals;
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
import com.messaggi.cache.ApplicationPlatformDevicesImpl.DeviceKey;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class TestApplicationPlatformDevices extends ApplicationPlatformCacheTestCase
{
    private static Device d1;

    private static Device d2;

    private static Device d3;

    private static Device d4;

    private static Device d5;

    private static Device d6;

    private LoadingCache<Integer, LoadingCache<DeviceKey, Device>> cache;

    private static void setUpDevices() throws Exception
    {
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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        ApplicationPlatformCacheTestCase.setUpBeforeClass();
        setUpDevices();
    }

    private static void tearDownDevices() throws Exception
    {
        TestDataHelper.deleteDevice(d1);
        TestDataHelper.deleteDevice(d2);
        TestDataHelper.deleteDevice(d3);
        TestDataHelper.deleteDevice(d4);
        TestDataHelper.deleteDevice(d5);
        TestDataHelper.deleteDevice(d6);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        tearDownDevices();
        ApplicationPlatformCacheTestCase.tearDownAfterClass();
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
        cache = (LoadingCache<Integer, LoadingCache<DeviceKey, Device>>) cacheField
                .get(ApplicationPlatformDevices.Instance.getInstance());
    }

    private LoadingCache<DeviceKey, Device> getDeviceCacheReferenceForApplicationPlatform(Integer id) throws Exception
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

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map = cache.asMap();
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

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map = cache.asMap();
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

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map = cache.asMap();
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

            LoadingCache<DeviceKey, Device> deviceCache = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            assertEquals(0, deviceCache.size());

            ConcurrentMap<DeviceKey, Device> deviceMap = deviceCache.asMap();
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

    private long validateDeviceCache1(LoadingCache<DeviceKey, Device> deviceCache1, DeviceKey key1)
    {
        assertEquals(1, deviceCache1.size());

        ConcurrentMap<DeviceKey, Device> deviceMap1 = deviceCache1.asMap();
        assertEquals(1, deviceMap1.size());
        assertTrue(deviceMap1.containsKey(key1));

        long deviceLastLoadTime = 0;
        CacheStats deviceStats1 = deviceCache1.stats();
        assertEquals(0, deviceStats1.evictionCount());
        assertEquals(0, deviceStats1.hitCount());
        assertEquals(0, deviceStats1.hitRate(), EPSILON);
        assertEquals(1, deviceStats1.loadCount());
        assertEquals(0, deviceStats1.loadExceptionCount());
        assertEquals(0, deviceStats1.loadExceptionRate(), EPSILON);
        assertEquals(1, deviceStats1.loadSuccessCount());
        assertEquals(1, deviceStats1.missCount());
        assertEquals(1.0, deviceStats1.missRate(), EPSILON);
        assertEquals(1, deviceStats1.requestCount());
        assertTrue(deviceStats1.totalLoadTime() > deviceLastLoadTime);
        return deviceStats1.totalLoadTime();
    }

    private long validateDeviceCache2(LoadingCache<DeviceKey, Device> deviceCache2, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2)
    {
        assertEquals(2, deviceCache2.size());

        ConcurrentMap<DeviceKey, Device> deviceMap2 = deviceCache2.asMap();
        assertEquals(2, deviceMap2.size());
        assertTrue(deviceMap2.containsKey(key1));
        assertTrue(deviceMap2.containsKey(key2));

        CacheStats deviceStats2 = deviceCache2.stats();
        assertEquals(0, deviceStats2.evictionCount());
        assertEquals(0, deviceStats2.hitCount());
        assertEquals(0, deviceStats2.hitRate(), EPSILON);
        assertEquals(2, deviceStats2.loadCount());
        assertEquals(0, deviceStats2.loadExceptionCount());
        assertEquals(0, deviceStats2.loadExceptionRate(), EPSILON);
        assertEquals(2, deviceStats2.loadSuccessCount());
        assertEquals(2, deviceStats2.missCount());
        assertEquals(1.0, deviceStats2.missRate(), EPSILON);
        assertEquals(2, deviceStats2.requestCount());
        assertTrue(deviceStats2.totalLoadTime() > deviceLastLoadTime);
        return deviceStats2.totalLoadTime();
    }

    private long validateDeviceCache3(LoadingCache<DeviceKey, Device> deviceCache3, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2)
    {
        assertEquals(2, deviceCache3.size());

        ConcurrentMap<DeviceKey, Device> deviceMap3 = deviceCache3.asMap();
        assertEquals(2, deviceMap3.size());
        assertTrue(deviceMap3.containsKey(key1));
        assertTrue(deviceMap3.containsKey(key2));

        CacheStats deviceStats3 = deviceCache3.stats();
        assertEquals(0, deviceStats3.evictionCount());
        assertEquals(0, deviceStats3.hitCount());
        assertEquals(0, deviceStats3.hitRate(), EPSILON);
        assertEquals(3, deviceStats3.loadCount());
        assertEquals(1, deviceStats3.loadExceptionCount());
        assertEquals(.333333333333, deviceStats3.loadExceptionRate(), EPSILON);
        assertEquals(2, deviceStats3.loadSuccessCount());
        assertEquals(3, deviceStats3.missCount());
        assertEquals(1.0, deviceStats3.missRate(), EPSILON);
        assertEquals(3, deviceStats3.requestCount());
        assertTrue(deviceStats3.totalLoadTime() > deviceLastLoadTime);
        return deviceStats3.totalLoadTime();
    }

    private long validateDeviceCache3(LoadingCache<DeviceKey, Device> deviceCache3, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2, DeviceKey key3)
    {
        assertEquals(3, deviceCache3.size());

        ConcurrentMap<DeviceKey, Device> deviceMap3 = deviceCache3.asMap();
        assertEquals(3, deviceMap3.size());
        assertTrue(deviceMap3.containsKey(key1));
        assertTrue(deviceMap3.containsKey(key2));
        assertTrue(deviceMap3.containsKey(key3));

        CacheStats deviceStats3 = deviceCache3.stats();
        assertEquals(0, deviceStats3.evictionCount());
        assertEquals(0, deviceStats3.hitCount());
        assertEquals(0, deviceStats3.hitRate(), EPSILON);
        assertEquals(3, deviceStats3.loadCount());
        assertEquals(0, deviceStats3.loadExceptionCount());
        assertEquals(0, deviceStats3.loadExceptionRate(), EPSILON);
        assertEquals(3, deviceStats3.loadSuccessCount());
        assertEquals(3, deviceStats3.missCount());
        assertEquals(1.0, deviceStats3.missRate(), EPSILON);
        assertEquals(3, deviceStats3.requestCount());
        assertTrue(deviceStats3.totalLoadTime() > deviceLastLoadTime);
        return deviceStats3.totalLoadTime();
    }

    private long validateDeviceCache4(LoadingCache<DeviceKey, Device> deviceCache4, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2)
    {
        assertEquals(2, deviceCache4.size());

        ConcurrentMap<DeviceKey, Device> deviceMap4 = deviceCache4.asMap();
        assertEquals(2, deviceMap4.size());
        assertTrue(deviceMap4.containsKey(key1));
        assertTrue(deviceMap4.containsKey(key2));

        CacheStats deviceStats4 = deviceCache4.stats();
        assertEquals(0, deviceStats4.evictionCount());
        assertEquals(1, deviceStats4.hitCount());
        assertEquals(.25, deviceStats4.hitRate(), EPSILON);
        assertEquals(3, deviceStats4.loadCount());
        assertEquals(1, deviceStats4.loadExceptionCount());
        assertEquals(.333333333333, deviceStats4.loadExceptionRate(), EPSILON);
        assertEquals(2, deviceStats4.loadSuccessCount());
        assertEquals(3, deviceStats4.missCount());
        assertEquals(.75, deviceStats4.missRate(), EPSILON);
        assertEquals(4, deviceStats4.requestCount());
        assertEquals(deviceStats4.totalLoadTime(), deviceLastLoadTime);
        return deviceStats4.totalLoadTime();
    }

    private long validateDeviceCache4(LoadingCache<DeviceKey, Device> deviceCache4, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2, DeviceKey key3, DeviceKey key4)
    {
        assertEquals(4, deviceCache4.size());

        ConcurrentMap<DeviceKey, Device> deviceMap4 = deviceCache4.asMap();
        assertEquals(4, deviceMap4.size());
        assertTrue(deviceMap4.containsKey(key1));
        assertTrue(deviceMap4.containsKey(key2));
        assertTrue(deviceMap4.containsKey(key3));
        assertTrue(deviceMap4.containsKey(key4));

        CacheStats deviceStats4 = deviceCache4.stats();
        assertEquals(0, deviceStats4.evictionCount());
        assertEquals(0, deviceStats4.hitCount());
        assertEquals(0, deviceStats4.hitRate(), EPSILON);
        assertEquals(4, deviceStats4.loadCount());
        assertEquals(0, deviceStats4.loadExceptionCount());
        assertEquals(0, deviceStats4.loadExceptionRate(), EPSILON);
        assertEquals(4, deviceStats4.loadSuccessCount());
        assertEquals(4, deviceStats4.missCount());
        assertEquals(1.0, deviceStats4.missRate(), EPSILON);
        assertEquals(4, deviceStats4.requestCount());
        assertTrue(deviceStats4.totalLoadTime() > deviceLastLoadTime);
        return deviceStats4.totalLoadTime();
    }

    private long validateDeviceCache5(LoadingCache<DeviceKey, Device> deviceCache5, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2, DeviceKey key3, DeviceKey key4)
    {
        assertEquals(4, deviceCache5.size());

        ConcurrentMap<DeviceKey, Device> deviceMap5 = deviceCache5.asMap();
        assertEquals(4, deviceMap5.size());
        assertTrue(deviceMap5.containsKey(key1));
        assertTrue(deviceMap5.containsKey(key2));
        assertTrue(deviceMap5.containsKey(key3));
        assertTrue(deviceMap5.containsKey(key4));

        CacheStats deviceStats5 = deviceCache5.stats();
        assertEquals(0, deviceStats5.evictionCount());
        assertEquals(0, deviceStats5.hitCount());
        assertEquals(0, deviceStats5.hitRate(), EPSILON);
        assertEquals(5, deviceStats5.loadCount());
        assertEquals(1, deviceStats5.loadExceptionCount());
        assertEquals(.2, deviceStats5.loadExceptionRate(), EPSILON);
        assertEquals(4, deviceStats5.loadSuccessCount());
        assertEquals(5, deviceStats5.missCount());
        assertEquals(1.0, deviceStats5.missRate(), EPSILON);
        assertEquals(5, deviceStats5.requestCount());
        assertTrue(deviceStats5.totalLoadTime() > deviceLastLoadTime);
        return deviceStats5.totalLoadTime();
    }

    private long validateDeviceCache6(LoadingCache<DeviceKey, Device> deviceCache6, long deviceLastLoadTime,
            DeviceKey key1, DeviceKey key2, DeviceKey key3, DeviceKey key4)
    {
        assertEquals(4, deviceCache6.size());

        ConcurrentMap<DeviceKey, Device> deviceMap6 = deviceCache6.asMap();
        assertEquals(4, deviceMap6.size());
        assertTrue(deviceMap6.containsKey(key1));
        assertTrue(deviceMap6.containsKey(key2));
        assertTrue(deviceMap6.containsKey(key3));
        assertTrue(deviceMap6.containsKey(key4));

        CacheStats deviceStats6 = deviceCache6.stats();
        assertEquals(0, deviceStats6.evictionCount());
        assertEquals(1, deviceStats6.hitCount());
        assertEquals(1 / (double) 6, deviceStats6.hitRate(), EPSILON);
        assertEquals(5, deviceStats6.loadCount());
        assertEquals(1, deviceStats6.loadExceptionCount());
        assertEquals(.2, deviceStats6.loadExceptionRate(), EPSILON);
        assertEquals(4, deviceStats6.loadSuccessCount());
        assertEquals(5, deviceStats6.missCount());
        assertEquals(5 / (double) 6, deviceStats6.missRate(), EPSILON);
        assertEquals(6, deviceStats6.requestCount());
        assertEquals(deviceStats6.totalLoadTime(), deviceLastLoadTime);
        return deviceStats6.totalLoadTime();
    }

    @Test
    public void testLoadValidId() throws Exception
    {
        validateCacheInitialState(cache);

        int hits = 0;
        int requests = 0;

        Device d11 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
        requests++;
        assertEquals(d1.getCode(), d11.getCode());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map1 = cache.asMap();
        assertEquals(1, map1.size());
        assertTrue(map1.containsKey(appPlat1.getId()));

        long lastLoadTime = 0;
        CacheStats stats1 = cache.stats();
        assertEquals(0, stats1.evictionCount());
        assertEquals(hits, stats1.hitCount());
        assertEquals(hits / (double) requests, stats1.hitRate(), EPSILON);
        assertEquals(1, stats1.loadCount());
        assertEquals(0, stats1.loadExceptionCount());
        assertEquals(0, stats1.loadExceptionRate(), EPSILON);
        assertEquals(1, stats1.loadSuccessCount());
        assertEquals(requests - hits, stats1.missCount());
        assertEquals((requests - hits) / (double) requests, stats1.missRate(), EPSILON);
        assertEquals(requests, stats1.requestCount());
        assertTrue(stats1.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats1.totalLoadTime();

        DeviceKey d1Key = new DeviceKey(appPlat1.getId(), d1.getCode());
        DeviceKey d2Key = new DeviceKey(appPlat1.getId(), d2.getCode());

        LoadingCache<DeviceKey, Device> deviceCache1 = getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId());
        hits++;
        requests++;
        long deviceLastLoadTime = validateDeviceCache1(deviceCache1, d1Key);

        Device d21 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d2.getCode());
        hits++;
        requests++;
        assertEquals(d2.getCode(), d21.getCode());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map2 = cache.asMap();
        assertEquals(1, map2.size());
        assertTrue(map2.containsKey(appPlat1.getId()));

        CacheStats stats2 = cache.stats();
        assertEquals(0, stats2.evictionCount());
        assertEquals(hits, stats2.hitCount());
        assertEquals(hits / (double) requests, stats2.hitRate(), EPSILON);
        assertEquals(1, stats2.loadCount());
        assertEquals(0, stats2.loadExceptionCount());
        assertEquals(0, stats2.loadExceptionRate(), EPSILON);
        assertEquals(1, stats2.loadSuccessCount());
        assertEquals(requests - hits, stats2.missCount());
        assertEquals((requests - hits) / (double) requests, stats2.missRate(), EPSILON);
        assertEquals(requests, stats2.requestCount());
        assertEquals(stats2.totalLoadTime(), lastLoadTime);

        LoadingCache<DeviceKey, Device> deviceCache2 = getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache2(deviceCache2, deviceLastLoadTime, d1Key, d2Key);

        try {
            // Increment before because the getDevice call below will fail on the device load.
            hits++;
            requests++;
            ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d3.getCode());
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map3 = cache.asMap();
            assertEquals(1, map3.size());
            assertTrue(map3.containsKey(appPlat1.getId()));

            CacheStats stats3 = cache.stats();
            assertEquals(0, stats3.evictionCount());
            assertEquals(hits, stats3.hitCount());
            assertEquals(hits / (double) requests, stats3.hitRate(), EPSILON);
            assertEquals(1, stats3.loadCount());
            assertEquals(0, stats3.loadExceptionCount());
            assertEquals(0, stats3.loadExceptionRate(), EPSILON);
            assertEquals(1, stats3.loadSuccessCount());
            assertEquals(requests - hits, stats3.missCount());
            assertEquals((requests - hits) / (double) requests, stats3.missRate(), EPSILON);
            assertEquals(requests, stats3.requestCount());
            assertEquals(stats3.totalLoadTime(), lastLoadTime);

            LoadingCache<DeviceKey, Device> deviceCache3 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache3(deviceCache3, deviceLastLoadTime, d1Key, d2Key);
        } catch (Exception e) {
            fail("The call above should have thrown");
        }

        Device d41 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
        hits++;
        requests++;
        assertEquals(d1.getCode(), d41.getCode());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map4 = cache.asMap();
        assertEquals(1, map4.size());
        assertTrue(map4.containsKey(appPlat1.getId()));

        CacheStats stats4 = cache.stats();
        assertEquals(0, stats4.evictionCount());
        assertEquals(hits, stats4.hitCount());
        assertEquals(hits / (double) requests, stats4.hitRate(), EPSILON);
        assertEquals(1, stats4.loadCount());
        assertEquals(0, stats4.loadExceptionCount());
        assertEquals(0, stats4.loadExceptionRate(), EPSILON);
        assertEquals(1, stats4.loadSuccessCount());
        assertEquals(requests - hits, stats4.missCount());
        assertEquals((requests - hits) / (double) requests, stats4.missRate(), EPSILON);
        assertEquals(requests, stats4.requestCount());
        assertEquals(stats4.totalLoadTime(), lastLoadTime);
        lastLoadTime = stats4.totalLoadTime();

        LoadingCache<DeviceKey, Device> deviceCache4 = getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId());
        hits++;
        requests++;
        validateDeviceCache4(deviceCache4, deviceLastLoadTime, d1Key, d2Key);

        Device d12 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d3.getCode());
        requests++;
        assertEquals(d3.getCode(), d12.getCode());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map5 = cache.asMap();
        assertEquals(2, map5.size());
        assertTrue(map5.containsKey(appPlat1.getId()));
        assertTrue(map5.containsKey(appPlat2.getId()));

        CacheStats stats5 = cache.stats();
        assertEquals(0, stats5.evictionCount());
        assertEquals(hits, stats5.hitCount());
        assertEquals(hits / (double) requests, stats5.hitRate(), EPSILON);
        assertEquals(2, stats5.loadCount());
        assertEquals(0, stats5.loadExceptionCount());
        assertEquals(0, stats5.loadExceptionRate(), EPSILON);
        assertEquals(2, stats5.loadSuccessCount());
        assertEquals(requests - hits, stats5.missCount());
        assertEquals((requests - hits) / (double) requests, stats5.missRate(), EPSILON);
        assertEquals(requests, stats5.requestCount());
        assertTrue(stats5.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats5.totalLoadTime();

        DeviceKey d3Key = new DeviceKey(appPlat2.getId(), d3.getCode());
        DeviceKey d4Key = new DeviceKey(appPlat2.getId(), d4.getCode());

        LoadingCache<DeviceKey, Device> deviceCache5 = getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache1(deviceCache5, d3Key);

        Device d22 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d4.getCode());
        hits++;
        requests++;
        assertEquals(d4.getCode(), d22.getCode());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map6 = cache.asMap();
        assertEquals(2, map6.size());
        assertTrue(map6.containsKey(appPlat1.getId()));
        assertTrue(map6.containsKey(appPlat2.getId()));

        CacheStats stats6 = cache.stats();
        assertEquals(0, stats6.evictionCount());
        assertEquals(hits, stats6.hitCount());
        assertEquals(hits / (double) requests, stats6.hitRate(), EPSILON);
        assertEquals(2, stats6.loadCount());
        assertEquals(0, stats6.loadExceptionCount());
        assertEquals(0, stats6.loadExceptionRate(), EPSILON);
        assertEquals(2, stats6.loadSuccessCount());
        assertEquals(requests - hits, stats6.missCount());
        assertEquals((requests - hits) / (double) requests, stats6.missRate(), EPSILON);
        assertEquals(requests, stats6.requestCount());
        assertEquals(stats6.totalLoadTime(), lastLoadTime);
        lastLoadTime = stats6.totalLoadTime();

        LoadingCache<DeviceKey, Device> deviceCache6 = getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache2(deviceCache6, deviceLastLoadTime, d3Key, d4Key);

        try {
            // Increment before because the getDevice call below will fail on the device load.
            hits++;
            requests++;
            ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d1.getCode());
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map7 = cache.asMap();
            assertEquals(2, map7.size());
            assertTrue(map7.containsKey(appPlat1.getId()));
            assertTrue(map7.containsKey(appPlat2.getId()));

            CacheStats stats7 = cache.stats();
            assertEquals(0, stats7.evictionCount());
            assertEquals(hits, stats7.hitCount());
            assertEquals(hits / (double) requests, stats7.hitRate(), EPSILON);
            assertEquals(2, stats7.loadCount());
            assertEquals(0, stats7.loadExceptionCount());
            assertEquals(0, stats7.loadExceptionRate(), EPSILON);
            assertEquals(2, stats7.loadSuccessCount());
            assertEquals(requests - hits, stats7.missCount());
            assertEquals((requests - hits) / (double) requests, stats7.missRate(), EPSILON);
            assertEquals(requests, stats7.requestCount());
            assertEquals(stats7.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats7.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache7 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache3(deviceCache7, deviceLastLoadTime, d3Key, d4Key);
        } catch (Exception e) {
            fail("The call above should have thrown");
        }

        Device d42 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d4.getCode());
        hits++;
        requests++;
        assertEquals(d4.getCode(), d42.getCode());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map8 = cache.asMap();
        assertEquals(2, map8.size());
        assertTrue(map8.containsKey(appPlat1.getId()));
        assertTrue(map8.containsKey(appPlat2.getId()));

        CacheStats stats8 = cache.stats();
        assertEquals(0, stats8.evictionCount());
        assertEquals(hits, stats8.hitCount());
        assertEquals(hits / (double) requests, stats8.hitRate(), EPSILON);
        assertEquals(2, stats8.loadCount());
        assertEquals(0, stats8.loadExceptionCount());
        assertEquals(0, stats8.loadExceptionRate(), EPSILON);
        assertEquals(2, stats8.loadSuccessCount());
        assertEquals(requests - hits, stats8.missCount());
        assertEquals((requests - hits) / (double) requests, stats8.missRate(), EPSILON);
        assertEquals(requests, stats8.requestCount());
        assertEquals(stats8.totalLoadTime(), lastLoadTime);
        lastLoadTime = stats8.totalLoadTime();

        LoadingCache<DeviceKey, Device> deviceCache8 = getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache4(deviceCache8, deviceLastLoadTime, d3Key, d4Key);

        Device d13 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d5.getCode());
        requests++;
        assertEquals(d5.getCode(), d13.getCode());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map9 = cache.asMap();
        assertEquals(2, map9.size());
        assertTrue(map9.containsKey(appPlat2.getId()));
        assertTrue(map9.containsKey(appPlat3.getId()));

        CacheStats stats9 = cache.stats();
        assertEquals(1, stats9.evictionCount());
        assertEquals(hits, stats9.hitCount());
        assertEquals(hits / (double) requests, stats9.hitRate(), EPSILON);
        assertEquals(3, stats9.loadCount());
        assertEquals(0, stats9.loadExceptionCount());
        assertEquals(0, stats9.loadExceptionRate(), EPSILON);
        assertEquals(3, stats9.loadSuccessCount());
        assertEquals(requests - hits, stats9.missCount());
        assertEquals((requests - hits) / (double) requests, stats9.missRate(), EPSILON);
        assertEquals(requests, stats9.requestCount());
        assertTrue(stats9.totalLoadTime() > lastLoadTime);
        lastLoadTime = stats9.totalLoadTime();

        DeviceKey d5Key = new DeviceKey(appPlat3.getId(), d5.getCode());
        DeviceKey d6Key = new DeviceKey(appPlat3.getId(), d6.getCode());

        LoadingCache<DeviceKey, Device> deviceCache9 = getDeviceCacheReferenceForApplicationPlatform(appPlat3.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache1(deviceCache9, d5Key);

        Device d23 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d6.getCode());
        hits++;
        requests++;
        assertEquals(d6.getCode(), d23.getCode());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map10 = cache.asMap();
        assertEquals(2, map10.size());
        assertTrue(map10.containsKey(appPlat2.getId()));
        assertTrue(map10.containsKey(appPlat3.getId()));

        CacheStats stats10 = cache.stats();
        assertEquals(1, stats10.evictionCount());
        assertEquals(hits, stats10.hitCount());
        assertEquals(hits / (double) requests, stats10.hitRate(), EPSILON);
        assertEquals(3, stats10.loadCount());
        assertEquals(0, stats10.loadExceptionCount());
        assertEquals(0, stats10.loadExceptionRate(), EPSILON);
        assertEquals(3, stats10.loadSuccessCount());
        assertEquals(requests - hits, stats10.missCount());
        assertEquals((requests - hits) / (double) requests, stats10.missRate(), EPSILON);
        assertEquals(requests, stats10.requestCount());
        assertEquals(stats10.totalLoadTime(), lastLoadTime);
        lastLoadTime = stats10.totalLoadTime();

        LoadingCache<DeviceKey, Device> deviceCache10 = getDeviceCacheReferenceForApplicationPlatform(appPlat3.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache2(deviceCache10, deviceLastLoadTime, d5Key, d6Key);

        try {
            // Increment before because the getDevice call below will fail on the device load.
            hits++;
            requests++;
            ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d1.getCode());
            fail("The call above should have thrown");
        } catch (InvalidCacheLoadException e) {
            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map11 = cache.asMap();
            assertEquals(2, map11.size());
            assertTrue(map11.containsKey(appPlat2.getId()));
            assertTrue(map11.containsKey(appPlat3.getId()));

            CacheStats stats11 = cache.stats();
            assertEquals(1, stats11.evictionCount());
            assertEquals(hits, stats11.hitCount());
            assertEquals(hits / (double) requests, stats11.hitRate(), EPSILON);
            assertEquals(3, stats11.loadCount());
            assertEquals(0, stats11.loadExceptionCount());
            assertEquals(0, stats11.loadExceptionRate(), EPSILON);
            assertEquals(3, stats11.loadSuccessCount());
            assertEquals(requests - hits, stats11.missCount());
            assertEquals((requests - hits) / (double) requests, stats11.missRate(), EPSILON);
            assertEquals(requests, stats11.requestCount());
            assertEquals(stats11.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats11.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache11 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache3(deviceCache11, deviceLastLoadTime, d5Key, d6Key);
        } catch (Exception e) {
            fail("The call above should have thrown");
        }

        Device d43 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d5.getCode());
        hits++;
        requests++;
        assertEquals(d5.getCode(), d43.getCode());

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map12 = cache.asMap();
        assertEquals(2, map12.size());
        assertTrue(map12.containsKey(appPlat2.getId()));
        assertTrue(map12.containsKey(appPlat3.getId()));

        CacheStats stats12 = cache.stats();
        assertEquals(1, stats12.evictionCount());
        assertEquals(hits, stats12.hitCount());
        assertEquals(hits / (double) requests, stats12.hitRate(), EPSILON);
        assertEquals(3, stats12.loadCount());
        assertEquals(0, stats12.loadExceptionCount());
        assertEquals(0, stats12.loadExceptionRate(), EPSILON);
        assertEquals(3, stats12.loadSuccessCount());
        assertEquals(requests - hits, stats12.missCount());
        assertEquals((requests - hits) / (double) requests, stats12.missRate(), EPSILON);
        assertEquals(requests, stats12.requestCount());
        assertEquals(stats12.totalLoadTime(), lastLoadTime);
        lastLoadTime = stats12.totalLoadTime();

        LoadingCache<DeviceKey, Device> deviceCache12 = getDeviceCacheReferenceForApplicationPlatform(appPlat3.getId());
        hits++;
        requests++;
        deviceLastLoadTime = validateDeviceCache4(deviceCache12, deviceLastLoadTime, d5Key, d6Key);
    }

    @Test
    public void testLoadValidIdWithDevicesInMultipleApplicationPlatforms() throws Exception
    {
        tearDownDevices();
        try {
            ApplicationPlatform[] appPlats1 = { appPlat1, appPlat3 };
            ApplicationPlatform[] appPlats2 = { appPlat1, appPlat2 };
            ApplicationPlatform[] appPlats3 = { appPlat2, appPlat3 };
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

            validateCacheInitialState(cache);

            int hits = 0;
            int requests = 0;

            Device d11 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
            requests++;
            assertEquals(d1.getCode(), d11.getCode());

            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map1 = cache.asMap();
            assertEquals(1, map1.size());
            assertTrue(map1.containsKey(appPlat1.getId()));

            long lastLoadTime = 0;
            CacheStats stats1 = cache.stats();
            assertEquals(0, stats1.evictionCount());
            assertEquals(hits, stats1.hitCount());
            assertEquals(hits / (double) requests, stats1.hitRate(), EPSILON);
            assertEquals(1, stats1.loadCount());
            assertEquals(0, stats1.loadExceptionCount());
            assertEquals(0, stats1.loadExceptionRate(), EPSILON);
            assertEquals(1, stats1.loadSuccessCount());
            assertEquals(requests - hits, stats1.missCount());
            assertEquals((requests - hits) / (double) requests, stats1.missRate(), EPSILON);
            assertEquals(requests, stats1.requestCount());
            assertTrue(stats1.totalLoadTime() > lastLoadTime);
            lastLoadTime = stats1.totalLoadTime();

            DeviceKey d1Key = new DeviceKey(appPlat1.getId(), d1.getCode());
            DeviceKey d2Key = new DeviceKey(appPlat1.getId(), d2.getCode());
            DeviceKey d3Key = new DeviceKey(appPlat1.getId(), d3.getCode());
            DeviceKey d4Key = new DeviceKey(appPlat1.getId(), d4.getCode());

            LoadingCache<DeviceKey, Device> deviceCache1 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            hits++;
            requests++;
            long deviceLastLoadTime = validateDeviceCache1(deviceCache1, d1Key);

            Device d21 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d2.getCode());
            hits++;
            requests++;
            assertEquals(d2.getCode(), d21.getCode());

            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map2 = cache.asMap();
            assertEquals(1, map2.size());
            assertTrue(map2.containsKey(appPlat1.getId()));

            CacheStats stats2 = cache.stats();
            assertEquals(0, stats2.evictionCount());
            assertEquals(hits, stats2.hitCount());
            assertEquals(hits / (double) requests, stats2.hitRate(), EPSILON);
            assertEquals(1, stats2.loadCount());
            assertEquals(0, stats2.loadExceptionCount());
            assertEquals(0, stats2.loadExceptionRate(), EPSILON);
            assertEquals(1, stats2.loadSuccessCount());
            assertEquals(requests - hits, stats2.missCount());
            assertEquals((requests - hits) / (double) requests, stats2.missRate(), EPSILON);
            assertEquals(requests, stats2.requestCount());
            assertEquals(stats2.totalLoadTime(), lastLoadTime);

            LoadingCache<DeviceKey, Device> deviceCache2 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache2(deviceCache2, deviceLastLoadTime, d1Key, d2Key);

            Device d31 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d3.getCode());
            hits++;
            requests++;
            assertEquals(d3.getCode(), d31.getCode());

            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map3 = cache.asMap();
            assertEquals(1, map3.size());
            assertTrue(map3.containsKey(appPlat1.getId()));

            CacheStats stats3 = cache.stats();
            assertEquals(0, stats3.evictionCount());
            assertEquals(hits, stats3.hitCount());
            assertEquals(hits / (double) requests, stats3.hitRate(), EPSILON);
            assertEquals(1, stats3.loadCount());
            assertEquals(0, stats3.loadExceptionCount());
            assertEquals(0, stats3.loadExceptionRate(), EPSILON);
            assertEquals(1, stats3.loadSuccessCount());
            assertEquals(requests - hits, stats3.missCount());
            assertEquals((requests - hits) / (double) requests, stats3.missRate(), EPSILON);
            assertEquals(requests, stats3.requestCount());
            assertEquals(stats3.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats3.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache3 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache3(deviceCache3, deviceLastLoadTime, d1Key, d2Key, d3Key);

            Device d41 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d4.getCode());
            hits++;
            requests++;
            assertEquals(d4.getCode(), d41.getCode());

            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map4 = cache.asMap();
            assertEquals(1, map4.size());
            assertTrue(map4.containsKey(appPlat1.getId()));

            CacheStats stats4 = cache.stats();
            assertEquals(0, stats4.evictionCount());
            assertEquals(hits, stats4.hitCount());
            assertEquals(hits / (double) requests, stats4.hitRate(), EPSILON);
            assertEquals(1, stats4.loadCount());
            assertEquals(0, stats4.loadExceptionCount());
            assertEquals(0, stats4.loadExceptionRate(), EPSILON);
            assertEquals(1, stats4.loadSuccessCount());
            assertEquals(requests - hits, stats4.missCount());
            assertEquals((requests - hits) / (double) requests, stats4.missRate(), EPSILON);
            assertEquals(requests, stats4.requestCount());
            assertEquals(stats4.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats4.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache4 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache4(deviceCache4, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);

            try {
                // Increment before because the getDevice call below will fail on the device load.
                hits++;
                requests++;
                ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d5.getCode());
                fail("The call above should have thrown");
            } catch (InvalidCacheLoadException e) {
                assertEquals(1, cache.size());

                ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map5 = cache.asMap();
                assertEquals(1, map5.size());
                assertTrue(map5.containsKey(appPlat1.getId()));

                CacheStats stats5 = cache.stats();
                assertEquals(0, stats5.evictionCount());
                assertEquals(hits, stats5.hitCount());
                assertEquals(hits / (double) requests, stats5.hitRate(), EPSILON);
                assertEquals(1, stats5.loadCount());
                assertEquals(0, stats5.loadExceptionCount());
                assertEquals(0, stats5.loadExceptionRate(), EPSILON);
                assertEquals(1, stats5.loadSuccessCount());
                assertEquals(requests - hits, stats5.missCount());
                assertEquals((requests - hits) / (double) requests, stats5.missRate(), EPSILON);
                assertEquals(requests, stats5.requestCount());
                assertEquals(stats5.totalLoadTime(), lastLoadTime);

                LoadingCache<DeviceKey, Device> deviceCache5 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                        .getId());
                hits++;
                requests++;
                deviceLastLoadTime = validateDeviceCache5(deviceCache5, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);
            } catch (Exception e) {
                fail("The call above should have thrown");
            }

            Device d61 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
            hits++;
            requests++;
            assertEquals(d1.getCode(), d61.getCode());

            assertEquals(1, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map6 = cache.asMap();
            assertEquals(1, map6.size());
            assertTrue(map6.containsKey(appPlat1.getId()));

            CacheStats stats6 = cache.stats();
            assertEquals(0, stats6.evictionCount());
            assertEquals(hits, stats6.hitCount());
            assertEquals(hits / (double) requests, stats6.hitRate(), EPSILON);
            assertEquals(1, stats6.loadCount());
            assertEquals(0, stats6.loadExceptionCount());
            assertEquals(0, stats6.loadExceptionRate(), EPSILON);
            assertEquals(1, stats6.loadSuccessCount());
            assertEquals(requests - hits, stats6.missCount());
            assertEquals((requests - hits) / (double) requests, stats6.missRate(), EPSILON);
            assertEquals(requests, stats6.requestCount());
            assertEquals(stats6.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats6.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache6 = getDeviceCacheReferenceForApplicationPlatform(appPlat1
                    .getId());
            hits++;
            requests++;
            validateDeviceCache6(deviceCache6, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);

            d1Key = new DeviceKey(appPlat2.getId(), d3.getCode());
            d2Key = new DeviceKey(appPlat2.getId(), d4.getCode());
            d3Key = new DeviceKey(appPlat2.getId(), d5.getCode());
            d4Key = new DeviceKey(appPlat2.getId(), d6.getCode());

            Device d7 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d3.getCode());
            requests++;
            assertEquals(d3.getCode(), d7.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map7 = cache.asMap();
            assertEquals(2, map7.size());
            assertTrue(map7.containsKey(appPlat1.getId()));
            assertTrue(map7.containsKey(appPlat2.getId()));

            CacheStats stats7 = cache.stats();
            assertEquals(0, stats7.evictionCount());
            assertEquals(hits, stats7.hitCount());
            assertEquals(hits / (double) requests, stats7.hitRate(), EPSILON);
            assertEquals(2, stats7.loadCount());
            assertEquals(0, stats7.loadExceptionCount());
            assertEquals(0, stats7.loadExceptionRate(), EPSILON);
            assertEquals(2, stats7.loadSuccessCount());
            assertEquals(requests - hits, stats7.missCount());
            assertEquals((requests - hits) / (double) requests, stats7.missRate(), EPSILON);
            assertEquals(requests, stats7.requestCount());
            assertTrue(stats7.totalLoadTime() > lastLoadTime);
            lastLoadTime = stats7.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache7 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache1(deviceCache7, d1Key);

            Device d22 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d4.getCode());
            hits++;
            requests++;
            assertEquals(d4.getCode(), d22.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map8 = cache.asMap();
            assertEquals(2, map8.size());
            assertTrue(map8.containsKey(appPlat1.getId()));
            assertTrue(map8.containsKey(appPlat2.getId()));

            CacheStats stats8 = cache.stats();
            assertEquals(0, stats8.evictionCount());
            assertEquals(hits, stats8.hitCount());
            assertEquals(hits / (double) requests, stats8.hitRate(), EPSILON);
            assertEquals(2, stats8.loadCount());
            assertEquals(0, stats8.loadExceptionCount());
            assertEquals(0, stats8.loadExceptionRate(), EPSILON);
            assertEquals(2, stats8.loadSuccessCount());
            assertEquals(requests - hits, stats8.missCount());
            assertEquals((requests - hits) / (double) requests, stats8.missRate(), EPSILON);
            assertEquals(requests, stats8.requestCount());
            assertEquals(stats8.totalLoadTime(), lastLoadTime);

            LoadingCache<DeviceKey, Device> deviceCache8 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache2(deviceCache8, deviceLastLoadTime, d1Key, d2Key);

            Device d32 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d5.getCode());
            hits++;
            requests++;
            assertEquals(d5.getCode(), d32.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map9 = cache.asMap();
            assertEquals(2, map9.size());
            assertTrue(map9.containsKey(appPlat1.getId()));
            assertTrue(map9.containsKey(appPlat2.getId()));

            CacheStats stats9 = cache.stats();
            assertEquals(0, stats9.evictionCount());
            assertEquals(hits, stats9.hitCount());
            assertEquals(hits / (double) requests, stats9.hitRate(), EPSILON);
            assertEquals(2, stats9.loadCount());
            assertEquals(0, stats9.loadExceptionCount());
            assertEquals(0, stats9.loadExceptionRate(), EPSILON);
            assertEquals(2, stats9.loadSuccessCount());
            assertEquals(requests - hits, stats9.missCount());
            assertEquals((requests - hits) / (double) requests, stats9.missRate(), EPSILON);
            assertEquals(requests, stats9.requestCount());
            assertEquals(stats9.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats9.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache9 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache3(deviceCache9, deviceLastLoadTime, d1Key, d2Key, d3Key);

            Device d42 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d6.getCode());
            hits++;
            requests++;
            assertEquals(d6.getCode(), d42.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map10 = cache.asMap();
            assertEquals(2, map10.size());
            assertTrue(map10.containsKey(appPlat1.getId()));
            assertTrue(map10.containsKey(appPlat2.getId()));

            CacheStats stats10 = cache.stats();
            assertEquals(0, stats10.evictionCount());
            assertEquals(hits, stats10.hitCount());
            assertEquals(hits / (double) requests, stats10.hitRate(), EPSILON);
            assertEquals(2, stats10.loadCount());
            assertEquals(0, stats10.loadExceptionCount());
            assertEquals(0, stats10.loadExceptionRate(), EPSILON);
            assertEquals(2, stats10.loadSuccessCount());
            assertEquals(requests - hits, stats10.missCount());
            assertEquals((requests - hits) / (double) requests, stats10.missRate(), EPSILON);
            assertEquals(requests, stats10.requestCount());
            assertEquals(stats10.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats10.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache10 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache4(deviceCache10, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);

            try {
                // Increment before because the getDevice call below will fail on the device load.
                hits++;
                requests++;
                ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d1.getCode());
                fail("The call above should have thrown");
            } catch (InvalidCacheLoadException e) {
                assertEquals(2, cache.size());

                ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map11 = cache.asMap();
                assertEquals(2, map11.size());
                assertTrue(map11.containsKey(appPlat1.getId()));
                assertTrue(map11.containsKey(appPlat2.getId()));

                CacheStats stats11 = cache.stats();
                assertEquals(0, stats11.evictionCount());
                assertEquals(hits, stats11.hitCount());
                assertEquals(hits / (double) requests, stats11.hitRate(), EPSILON);
                assertEquals(2, stats11.loadCount());
                assertEquals(0, stats11.loadExceptionCount());
                assertEquals(0, stats11.loadExceptionRate(), EPSILON);
                assertEquals(2, stats11.loadSuccessCount());
                assertEquals(requests - hits, stats11.missCount());
                assertEquals((requests - hits) / (double) requests, stats11.missRate(), EPSILON);
                assertEquals(requests, stats11.requestCount());
                assertEquals(stats11.totalLoadTime(), lastLoadTime);

                LoadingCache<DeviceKey, Device> deviceCache11 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                        .getId());
                hits++;
                requests++;
                deviceLastLoadTime = validateDeviceCache5(deviceCache11, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);
            } catch (Exception e) {
                fail("The call above should have thrown");
            }

            Device d62 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat2.getId(), d3.getCode());
            hits++;
            requests++;
            assertEquals(d3.getCode(), d62.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map12 = cache.asMap();
            assertEquals(2, map12.size());
            assertTrue(map12.containsKey(appPlat2.getId()));

            CacheStats stats12 = cache.stats();
            assertEquals(0, stats12.evictionCount());
            assertEquals(hits, stats12.hitCount());
            assertEquals(hits / (double) requests, stats12.hitRate(), EPSILON);
            assertEquals(2, stats12.loadCount());
            assertEquals(0, stats12.loadExceptionCount());
            assertEquals(0, stats12.loadExceptionRate(), EPSILON);
            assertEquals(2, stats12.loadSuccessCount());
            assertEquals(requests - hits, stats12.missCount());
            assertEquals((requests - hits) / (double) requests, stats12.missRate(), EPSILON);
            assertEquals(requests, stats12.requestCount());
            assertEquals(stats12.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats12.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache12 = getDeviceCacheReferenceForApplicationPlatform(appPlat2
                    .getId());
            hits++;
            requests++;
            validateDeviceCache6(deviceCache12, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);
            
            d1Key = new DeviceKey(appPlat3.getId(), d1.getCode());
            d2Key = new DeviceKey(appPlat3.getId(), d2.getCode());
            d3Key = new DeviceKey(appPlat3.getId(), d5.getCode());
            d4Key = new DeviceKey(appPlat3.getId(), d6.getCode());

            Device d13 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d1.getCode());
            requests++;
            assertEquals(d1.getCode(), d13.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map13 = cache.asMap();
            assertEquals(2, map13.size());
            assertTrue(map13.containsKey(appPlat2.getId()));
            assertTrue(map13.containsKey(appPlat3.getId()));

            CacheStats stats13 = cache.stats();
            assertEquals(1, stats13.evictionCount());
            assertEquals(hits, stats13.hitCount());
            assertEquals(hits / (double) requests, stats13.hitRate(), EPSILON);
            assertEquals(3, stats13.loadCount());
            assertEquals(0, stats13.loadExceptionCount());
            assertEquals(0, stats13.loadExceptionRate(), EPSILON);
            assertEquals(3, stats13.loadSuccessCount());
            assertEquals(requests - hits, stats13.missCount());
            assertEquals((requests - hits) / (double) requests, stats13.missRate(), EPSILON);
            assertEquals(requests, stats13.requestCount());
            assertTrue(stats13.totalLoadTime() > lastLoadTime);
            lastLoadTime = stats13.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache13 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache1(deviceCache13, d1Key);

            Device d23 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d2.getCode());
            hits++;
            requests++;
            assertEquals(d2.getCode(), d23.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map14 = cache.asMap();
            assertEquals(2, map14.size());
            assertTrue(map14.containsKey(appPlat2.getId()));
            assertTrue(map14.containsKey(appPlat3.getId()));

            CacheStats stats14 = cache.stats();
            assertEquals(1, stats14.evictionCount());
            assertEquals(hits, stats14.hitCount());
            assertEquals(hits / (double) requests, stats14.hitRate(), EPSILON);
            assertEquals(3, stats14.loadCount());
            assertEquals(0, stats14.loadExceptionCount());
            assertEquals(0, stats14.loadExceptionRate(), EPSILON);
            assertEquals(3, stats14.loadSuccessCount());
            assertEquals(requests - hits, stats14.missCount());
            assertEquals((requests - hits) / (double) requests, stats14.missRate(), EPSILON);
            assertEquals(requests, stats14.requestCount());
            assertEquals(stats14.totalLoadTime(), lastLoadTime);

            LoadingCache<DeviceKey, Device> deviceCache14 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache2(deviceCache14, deviceLastLoadTime, d1Key, d2Key);

            Device d33 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d5.getCode());
            hits++;
            requests++;
            assertEquals(d5.getCode(), d33.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map15 = cache.asMap();
            assertEquals(2, map15.size());
            assertTrue(map15.containsKey(appPlat2.getId()));
            assertTrue(map15.containsKey(appPlat3.getId()));

            CacheStats stats15 = cache.stats();
            assertEquals(1, stats15.evictionCount());
            assertEquals(hits, stats15.hitCount());
            assertEquals(hits / (double) requests, stats15.hitRate(), EPSILON);
            assertEquals(3, stats15.loadCount());
            assertEquals(0, stats15.loadExceptionCount());
            assertEquals(0, stats15.loadExceptionRate(), EPSILON);
            assertEquals(3, stats15.loadSuccessCount());
            assertEquals(requests - hits, stats15.missCount());
            assertEquals((requests - hits) / (double) requests, stats15.missRate(), EPSILON);
            assertEquals(requests, stats15.requestCount());
            assertEquals(stats15.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats15.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache15 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache3(deviceCache15, deviceLastLoadTime, d1Key, d2Key, d3Key);

            Device d43 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d6.getCode());
            hits++;
            requests++;
            assertEquals(d6.getCode(), d43.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map16 = cache.asMap();
            assertEquals(2, map16.size());
            assertTrue(map16.containsKey(appPlat2.getId()));
            assertTrue(map16.containsKey(appPlat3.getId()));

            CacheStats stats16 = cache.stats();
            assertEquals(1, stats16.evictionCount());
            assertEquals(hits, stats16.hitCount());
            assertEquals(hits / (double) requests, stats16.hitRate(), EPSILON);
            assertEquals(3, stats16.loadCount());
            assertEquals(0, stats16.loadExceptionCount());
            assertEquals(0, stats16.loadExceptionRate(), EPSILON);
            assertEquals(3, stats16.loadSuccessCount());
            assertEquals(requests - hits, stats16.missCount());
            assertEquals((requests - hits) / (double) requests, stats16.missRate(), EPSILON);
            assertEquals(requests, stats16.requestCount());
            assertEquals(stats16.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats16.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache16 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                    .getId());
            hits++;
            requests++;
            deviceLastLoadTime = validateDeviceCache4(deviceCache16, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);

            try {
                // Increment before because the getDevice call below will fail on the device load.
                hits++;
                requests++;
                ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d3.getCode());
                fail("The call above should have thrown");
            } catch (InvalidCacheLoadException e) {
                assertEquals(2, cache.size());

                ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map17 = cache.asMap();
                assertEquals(2, map17.size());
                assertTrue(map17.containsKey(appPlat2.getId()));
                assertTrue(map17.containsKey(appPlat3.getId()));

                CacheStats stats17 = cache.stats();
                assertEquals(1, stats17.evictionCount());
                assertEquals(hits, stats17.hitCount());
                assertEquals(hits / (double) requests, stats17.hitRate(), EPSILON);
                assertEquals(3, stats17.loadCount());
                assertEquals(0, stats17.loadExceptionCount());
                assertEquals(0, stats17.loadExceptionRate(), EPSILON);
                assertEquals(3, stats17.loadSuccessCount());
                assertEquals(requests - hits, stats17.missCount());
                assertEquals((requests - hits) / (double) requests, stats17.missRate(), EPSILON);
                assertEquals(requests, stats17.requestCount());
                assertEquals(stats17.totalLoadTime(), lastLoadTime);

                LoadingCache<DeviceKey, Device> deviceCache17 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                        .getId());
                hits++;
                requests++;
                deviceLastLoadTime = validateDeviceCache5(deviceCache17, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);
            }

            Device d63 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat3.getId(), d1.getCode());
            hits++;
            requests++;
            assertEquals(d1.getCode(), d63.getCode());

            assertEquals(2, cache.size());

            ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map18 = cache.asMap();
            assertEquals(2, map18.size());
            assertTrue(map18.containsKey(appPlat2.getId()));

            CacheStats stats18 = cache.stats();
            assertEquals(1, stats18.evictionCount());
            assertEquals(hits, stats18.hitCount());
            assertEquals(hits / (double) requests, stats18.hitRate(), EPSILON);
            assertEquals(3, stats18.loadCount());
            assertEquals(0, stats18.loadExceptionCount());
            assertEquals(0, stats18.loadExceptionRate(), EPSILON);
            assertEquals(3, stats18.loadSuccessCount());
            assertEquals(requests - hits, stats18.missCount());
            assertEquals((requests - hits) / (double) requests, stats18.missRate(), EPSILON);
            assertEquals(requests, stats18.requestCount());
            assertEquals(stats18.totalLoadTime(), lastLoadTime);
            lastLoadTime = stats18.totalLoadTime();

            LoadingCache<DeviceKey, Device> deviceCache18 = getDeviceCacheReferenceForApplicationPlatform(appPlat3
                    .getId());
            hits++;
            requests++;
            validateDeviceCache6(deviceCache18, deviceLastLoadTime, d1Key, d2Key, d3Key, d4Key);
        } finally {
            tearDownDevices();
            setUpDevices();
        }
    }

    @Test
    public void testCreateDeviceCacheForAllApplicationPlatforms() throws Exception
    {
        int hits = 0;
        int requests = 0;

        ApplicationPlatformDevices.Instance.getInstance().createDeviceCacheForAllApplicationPlatforms(
                Arrays.asList(new Integer[] { appPlat1.getId(), appPlat2.getId() }));
        requests += 2;

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map1 = cache.asMap();
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

        List<LoadingCache<DeviceKey, Device>> deviceCaches1 = new ArrayList<>();
        deviceCaches1.add(getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId()));
        hits++;
        requests++;
        deviceCaches1.add(getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        for (LoadingCache<DeviceKey, Device> deviceCache : deviceCaches1) {
            assertEquals(0, deviceCache.size());

            ConcurrentMap<DeviceKey, Device> deviceMap = deviceCache.asMap();
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
        hits += 2;
        requests += 2;

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map2 = cache.asMap();
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
        
        List<LoadingCache<DeviceKey, Device>> deviceCaches2 = new ArrayList<>();
        deviceCaches2.add(getDeviceCacheReferenceForApplicationPlatform(appPlat1.getId()));
        hits++;
        requests++;
        deviceCaches2.add(getDeviceCacheReferenceForApplicationPlatform(appPlat2.getId()));
        hits++;
        requests++;
        for (LoadingCache<DeviceKey, Device> deviceCache : deviceCaches2) {
	        assertEquals(0, deviceCache.size());
	
            ConcurrentMap<DeviceKey, Device> deviceMap = deviceCache.asMap();
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
        requests += 2;

        assertEquals(2, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map3 = cache.asMap();
        assertEquals(2, map3.size());
        assertTrue(map3.containsKey(appPlat3.getId()));
        assertTrue(map3.containsKey(appPlat4.getId()));

        CacheStats stats3 = cache.stats();
        assertEquals(2, stats3.evictionCount());
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

        List<LoadingCache<DeviceKey, Device>> deviceCaches3 = new ArrayList<>();
        deviceCaches3.add(getDeviceCacheReferenceForApplicationPlatform(appPlat3.getId()));
        hits++;
        requests++;
        deviceCaches3.add(getDeviceCacheReferenceForApplicationPlatform(appPlat4.getId()));
        hits++;
        requests++;
        for (LoadingCache<DeviceKey, Device> deviceCache : deviceCaches3) {
            assertEquals(0, deviceCache.size());

            ConcurrentMap<DeviceKey, Device> deviceMap = deviceCache.asMap();
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

    @Test
    public void testInitialize() throws Exception
    {
        validateCacheInitialState(cache);

        Device d11 = ApplicationPlatformDevices.Instance.getInstance().getDevice(appPlat1.getId(), d1.getCode());
        assertEquals(d1.getCode(), d11.getCode());

        assertEquals(1, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map1 = cache.asMap();
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
        ApplicationPlatformDevices.Instance.getInstance().initialize(cip);
        createCacheReference();

        assertEquals(0, cache.size());

        ConcurrentMap<Integer, LoadingCache<DeviceKey, Device>> map2 = cache.asMap();
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

