package com.messaggi.cache;

import java.util.concurrent.ExecutionException;

import com.messaggi.domain.Device;

/**
 * Cache of devices per application platform. This is a multi-level cache where
 * the first level is for application platforms and the second level is for
 * devices. To get a device you must do so through the relevant application
 * platform.
 */
public interface ApplicationPlatformDevicesCache
{
    void createDeviceCacheForAllApplicationPlatforms(Iterable<? extends Integer> ids) throws ExecutionException;

    Device getDevice(Integer applicationPlatformId, String deviceCode) throws ExecutionException;

    void initialize(CacheInitializationParameters initParams);
}

