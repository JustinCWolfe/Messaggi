package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.messaggi.domain.Device;

public class ApplicationPlatformDevices
{
    private static final String CACHE_JNDI_NAME = "messaggi:/cache/ApplicationPlatformDevicesCache";

    private static final ApplicationPlatformDevicesCache cache;

    static {
        try {
            cache = (ApplicationPlatformDevicesCache) InitialContext.doLookup(CACHE_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + CACHE_JNDI_NAME, e);
        }
    }

    public static Device getDevice(Integer applicationPlatformId, String deviceCode) throws ExecutionException
    {
        return cache.getDevice(applicationPlatformId, deviceCode);
    }
}

