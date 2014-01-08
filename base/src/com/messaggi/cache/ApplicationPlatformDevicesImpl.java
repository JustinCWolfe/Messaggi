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
import com.messaggi.dao.DeviceDAO;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class ApplicationPlatformDevicesImpl implements ApplicationPlatformDevices
{
    private static final ApplicationPlatformDAO applicationPlatformsDao;

    private static final DeviceDAO devicesDao;

    private static final CacheLoader<Integer, LoadingCache<String, Device>> applicationPlatformsCacheLoader;

    private static final CacheLoader<String, Device> devicesCacheLoader;

    private LoadingCache<Integer, LoadingCache<String, Device>> cache;

    static {
        applicationPlatformsDao = new ApplicationPlatformDAO();
        devicesDao = new DeviceDAO();
        devicesCacheLoader = createDevicesCacheLoader();
        applicationPlatformsCacheLoader = createApplicationPlatformsCacheLoader();
    }

    private ApplicationPlatformDevicesImpl()
    {
        initialize(CacheInitializationParameters.DEFAULT_INIT_PARAMS);
    }

    private static CacheLoader<String, Device> createDevicesCacheLoader()
    {
        CacheLoader<String, Device> cacheLoader = new CacheLoader<String, Device>()
        {
            @Override
            public Device load(String code) throws Exception
            {
                List<Device> retrieved = devicesDao.getDevice(new Device[] { createDevicePrototype(code) });
                return (retrieved.size() == 1) ? retrieved.get(0) : null;
            }

            @Override
            public Map<String, Device> loadAll(Iterable<? extends String> codes) throws Exception
            {
                List<Device> prototypes = new ArrayList<>();
                for (String code : codes) {
                    prototypes.add(createDevicePrototype(code));
                }
                List<Device> retrieved = devicesDao.getDevice(prototypes.toArray(new Device[prototypes.size()]));
                Map<String, Device> retrievedMap = null;
                if (retrieved.size() > 0) {
                    retrievedMap = new HashMap<>();
                    for (Device d : retrieved) {
                        retrievedMap.put(d.getCode(), d);
                    }
                }
                return retrievedMap;
            }
        };
        return cacheLoader;
    }

    private static CacheLoader<Integer, LoadingCache<String, Device>> createApplicationPlatformsCacheLoader()
    {
        CacheLoader<Integer, LoadingCache<String, Device>> cacheLoader = new CacheLoader<Integer, LoadingCache<String, Device>>()
        {
            @Override
            public LoadingCache<String, Device> load(Integer id) throws Exception
            {
                // What this should do is load the application platform.  This application platform could have 
                // configuration options in attribute columns (these aren't there today but I might add them so 
                // that applications can have device caching specific to their needs). 
                List<ApplicationPlatform> retrieved = applicationPlatformsDao
                        .getApplicationPlatform(new ApplicationPlatform[] { createApplicationPlatformPrototype(id) });
                if (retrieved.size() == 1) {
                    //TODO: get the maximum size for this application platforms' device cache from the domain object.
                    int maxSize = 1000;
                    //TODO: get the record stats for this application platforms' device cache from the domain object.
                    boolean isRecordStats = true;
                    CacheBuilder<Object, Object> devicesCacheBuilder = CacheBuilder.newBuilder().maximumSize(maxSize);
                    if (isRecordStats) {
                        devicesCacheBuilder.recordStats();
                    }
                    return devicesCacheBuilder.build(devicesCacheLoader);
                }
                throw new InvalidCacheLoadException("Could not load application platform for id: " + id);
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

    private static Device createDevicePrototype(String code)
    {
        Device prototype = new Device();
        prototype.setCode(code);
        return prototype;
    }

    @Override
    public Device getDevice(Integer applicationPlatformId, String deviceCode) throws ExecutionException
    {
        LoadingCache<String, Device> deviceCache = cache.get(applicationPlatformId);
        return deviceCache.get(deviceCode);
    }

    @Override
    public void initialize(CacheInitializationParameters initParams)
    {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().maximumSize(initParams.getMaxSize());
        if (initParams.isRecordStats()) {
            builder.recordStats();
        }
        cache = builder.build(applicationPlatformsCacheLoader);
    }
}

