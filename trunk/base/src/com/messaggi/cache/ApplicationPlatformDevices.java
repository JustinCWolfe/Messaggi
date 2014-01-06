package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.ApplicationPlatformDAO;
import com.messaggi.dao.DeviceDAO;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

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

    /**
     * We have a devices class (and devices cache) for each application
     * platform. Doing it this way would allow me to configure the device
     * caching on a per-application or per-application-platform basis. For
     * example, if an application needed a larger devices cache it could be set
     * as a parameter on the application platform record (new column) and we
     * could use that application platform attribute to configure the devices
     * cache.
     */
    private LoadingCache<Integer, Devices> createApplicationPlatformDeviceCache()
    {
        CacheLoader<Integer, Devices> cacheLoader = new CacheLoader<Integer, Devices>()
        {
            @Override
            public Devices load(Integer id) throws Exception
            {
                return new Devices();
            }

            @Override
            public Map<Integer, Devices> loadAll(Iterable<? extends Integer> ids) throws Exception
            {
                Map<Integer, Devices> initializedMap = new HashMap<>();
                for (Integer id : ids) {
                    initializedMap.put(id, new Devices());
                }
                return initializedMap;
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
    
    public class Devices
    {
        private static final int DEFAULT_MAX_SIZE = 1000;
        
        private final LoadingCache<String, Device> deviceCache;

        private final DeviceDAO dao = new DeviceDAO();
        
        private Devices()
        {
            this(DEFAULT_MAX_SIZE);
        }

        private Devices(int maxSize)
        {
            deviceCache = createDeviceCache(maxSize);
        }

        private LoadingCache<String, Device> createDeviceCache(int maxSize)
        {
            CacheLoader<String, Device> cacheLoader = new CacheLoader<String, Device>()
            {
                @Override
                public Device load(String code) throws Exception
                {
                    List<Device> retrieved = dao.getDevice(new Device[] { createPrototype(code) });
                    return retrieved.get(0);
                }

                @Override
                public Map<String, Device> loadAll(Iterable<? extends String> codes) throws Exception
                {
                    List<Device> prototypes = new ArrayList<>();
                    for (String code : codes) {
                        prototypes.add(createPrototype(code));
                    }
                    List<Device> retrieved = dao.getDevice(prototypes.toArray(new Device[prototypes.size()]));
                    Map<String, Device> retrievedMap = new HashMap<>();
                    for (Device d : retrieved) {
                        retrievedMap.put(d.getCode(), d);
                    }
                    return retrievedMap;
                }
            };
            return CacheBuilder.newBuilder().maximumSize(maxSize).build(cacheLoader);
        }

        private Device createPrototype(String code)
        {
            Device prototype = new Device();
            prototype.setCode(code);
            return prototype;
        }
    }
}

