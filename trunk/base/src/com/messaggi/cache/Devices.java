package com.messaggi.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.messaggi.dao.DeviceDAO;
import com.messaggi.domain.Device;

public class Devices
{
    private final LoadingCache<String, Device> deviceCache;

    private final DeviceDAO dao = new DeviceDAO();

    public Devices()
    {
        deviceCache = createDeviceCache();
    }

    private LoadingCache<String, Device> createDeviceCache()
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
        return CacheBuilder.newBuilder().maximumSize(1000).build(cacheLoader);
    }

    private Device createPrototype(String code)
    {
        Device prototype = new Device();
        prototype.setCode(code);
        return prototype;
    }
}

