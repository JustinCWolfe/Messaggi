package com.messaggi.cache;

import java.util.List;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatformDevices
{
    private static ApplicationPlatformDevices instance;

    private final LoadingCache<Integer, Devices> applicationPlatformDeviceCache;

    private final ApplicationPlatformDAO dao = new ApplicationPlatformDAO();

    public static synchronized ApplicationPlatformDevices getInstance()
    {
        if (instance == null) {
            instance = new ApplicationPlatformDevices();
        }
        return instance;
    }

    private ApplicationPlatformDevices()
    {
        applicationPlatformDeviceCache = createApplicationPlatformDeviceCache();
    }

    private LoadingCache<Integer, Devices> createApplicationPlatformDeviceCache()
    {
        CacheLoader<Integer, Devices> cacheLoader = new CacheLoader<Integer, Devices>()
        {
            @Override
            public Devices load(Integer id) throws Exception
            {
                List<ApplicationPlatform> retrieved = dao
                        .getApplicationPlatform(new ApplicationPlatform[] { createPrototype(id) });
                return new Devices();
            }
        };
        return CacheBuilder.newBuilder().maximumSize(1000).build(cacheLoader);
    }

    private ApplicationPlatform createPrototype(Integer id)
    {
        ApplicationPlatform prototype = new ApplicationPlatform();
        prototype.setId(id);
        return prototype;
    }
}

