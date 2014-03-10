package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.dao.DeviceDAO;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class ApplicationPlatformDevicesCacheImpl implements ApplicationPlatformDevicesCache
{
    private static final ApplicationPlatformDAO applicationPlatformsDao;

    private static final DeviceDAO devicesDao;

    private static final CacheLoader<Integer, LoadingCache<DeviceKey, Device>> applicationPlatformCacheLoader;

    private static final CacheLoader<DeviceKey, Device> deviceCacheLoader;

    private LoadingCache<Integer, LoadingCache<DeviceKey, Device>> cache;

    static {
        applicationPlatformsDao = new ApplicationPlatformDAO();
        devicesDao = new DeviceDAO();
        deviceCacheLoader = createDeviceCacheLoader();
        applicationPlatformCacheLoader = createApplicationPlatformCacheLoader();
    }

    public ApplicationPlatformDevicesCacheImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private static CacheLoader<Integer, LoadingCache<DeviceKey, Device>> createApplicationPlatformCacheLoader()
    {
        CacheLoader<Integer, LoadingCache<DeviceKey, Device>> cacheLoader = new CacheLoader<Integer, LoadingCache<DeviceKey, Device>>()
        {
            private LoadingCache<DeviceKey, Device> createDeviceCache(ApplicationPlatform ap)
            {
                //TODO: get the maximum size for this application platforms' device cache from the domain object.
                int maxSize = 1000;
                //TODO: get the record stats for this application platforms' device cache from the domain object.
                boolean isRecordStats = true;
                CacheBuilder<Object, Object> deviceCacheBuilder = CacheBuilder.newBuilder().maximumSize(maxSize);
                if (isRecordStats) {
                    deviceCacheBuilder.recordStats();
                }
                return deviceCacheBuilder.build(deviceCacheLoader);
            }

            @Override
            public LoadingCache<DeviceKey, Device> load(Integer id) throws Exception
            {
                // What this should do is load the application platform.  This application platform could have 
                // configuration options in attribute columns (these aren't there today but I might add them so 
                // that applications can have device caching specific to their needs). 
                List<ApplicationPlatform> retrieved = applicationPlatformsDao
                        .getApplicationPlatform(new ApplicationPlatform[] { createApplicationPlatformPrototype(id) });
                if (retrieved.size() == 1) {
                    return createDeviceCache(retrieved.get(0));
                }
                return null;
            }

            @Override
            public Map<Integer, LoadingCache<DeviceKey, Device>> loadAll(Iterable<? extends Integer> ids)
                throws Exception
            {
                List<ApplicationPlatform> prototypes = new ArrayList<>();
                for (Integer id : ids) {
                    prototypes.add(createApplicationPlatformPrototype(id));
                }
                // What this should do is load the application platform.  This application platform could have 
                // configuration options in attribute columns (these aren't there today but I might add them so 
                // that applications can have device caching specific to their needs). 
                List<ApplicationPlatform> retrieved = applicationPlatformsDao.getApplicationPlatform(prototypes
                        .toArray(new ApplicationPlatform[prototypes.size()]));
                Map<Integer, LoadingCache<DeviceKey, Device>> retrievedMap = null;
                if (retrieved.size() > 0) {
                    retrievedMap = new HashMap<>();
                    for (ApplicationPlatform ap : retrieved) {
                        retrievedMap.put(ap.getId(), createDeviceCache(ap));
                    }
                }
                return retrievedMap;
            }
        };
        return cacheLoader;
    }

    private static CacheLoader<DeviceKey, Device> createDeviceCacheLoader()
    {
        CacheLoader<DeviceKey, Device> cacheLoader = new CacheLoader<DeviceKey, Device>()
        {
            @Override
            public Device load(DeviceKey key) throws Exception
            {
                List<Device> retrieved = devicesDao.getDevice(new Device[] { createDevicePrototype(key) });
                return (retrieved.size() == 1) ? retrieved.get(0) : null;
            }

            @Override
            public Map<DeviceKey, Device> loadAll(Iterable<? extends DeviceKey> keys) throws Exception
            {
                List<Device> prototypes = new ArrayList<>();
                for (DeviceKey key : keys) {
                    prototypes.add(createDevicePrototype(key));
                }
                List<Device> retrieved = devicesDao.getDevice(prototypes.toArray(new Device[prototypes.size()]));
                Map<DeviceKey, Device> retrievedMap = null;
                if (retrieved.size() > 0) {
                    retrievedMap = new HashMap<>();
                    for (Device d : retrieved) {
                        retrievedMap.put(new DeviceKey(d.getApplicationPlatformId(), d.getCode()), d);
                    }
                }
                return retrievedMap;
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

    private static Device createDevicePrototype(DeviceKey key)
    {
        Device prototype = new Device();
        HashSet<ApplicationPlatform> applicationPlatforms = new HashSet<>();
        applicationPlatforms.add(createApplicationPlatformPrototype(key.applicationPlatformId));
        prototype.setApplicationPlatforms(applicationPlatforms);
        prototype.setCode(key.deviceCode);
        return prototype;
    }

    @Override
    public void createDeviceCacheForAllApplicationPlatforms(Iterable<? extends Integer> ids) throws ExecutionException
    {
        cache.getAll(ids);
    }

    @Override
    public Device getDevice(Integer applicationPlatformId, String deviceCode) throws ExecutionException
    {
        LoadingCache<DeviceKey, Device> deviceCache = cache.get(applicationPlatformId);
        return deviceCache.get(new DeviceKey(applicationPlatformId, deviceCode));
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

    public static class DeviceKey
    {
        private final Integer applicationPlatformId;

        private final String deviceCode;

        public DeviceKey(Integer applicationPlatformId, String deviceCode)
        {
            this.applicationPlatformId = applicationPlatformId;
            this.deviceCode = deviceCode;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((applicationPlatformId == null) ? 0 : applicationPlatformId.hashCode());
            result = prime * result + ((deviceCode == null) ? 0 : deviceCode.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof DeviceKey))
                return false;
            DeviceKey other = (DeviceKey) obj;
            if (applicationPlatformId == null) {
                if (other.applicationPlatformId != null)
                    return false;
            } else if (!applicationPlatformId.equals(other.applicationPlatformId))
                return false;
            if (deviceCode == null) {
                if (other.deviceCode != null)
                    return false;
            } else if (!deviceCode.equals(other.deviceCode))
                return false;
            return true;
        }

    }
}

