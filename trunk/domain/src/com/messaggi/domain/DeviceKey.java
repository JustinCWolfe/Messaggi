package com.messaggi.domain;

// Generated Oct 8, 2013 1:37:50 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * DeviceKey generated by hbm2java
 */
public class DeviceKey implements java.io.Serializable
{

    private String key;

    private String description;

    private Set<DeviceAttribute> deviceAttributes = new HashSet<DeviceAttribute>(0);

    public DeviceKey()
    {
    }

    public DeviceKey(String key, String description)
    {
        this.key = key;
        this.description = description;
    }

    public DeviceKey(String key, String description, Set<DeviceAttribute> deviceAttributes)
    {
        this.key = key;
        this.description = description;
        this.deviceAttributes = deviceAttributes;
    }

    public String getKey()
    {
        return this.key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Set<DeviceAttribute> getDeviceAttributes()
    {
        return this.deviceAttributes;
    }

    public void setDeviceAttributes(Set<DeviceAttribute> deviceAttributes)
    {
        this.deviceAttributes = deviceAttributes;
    }

}
