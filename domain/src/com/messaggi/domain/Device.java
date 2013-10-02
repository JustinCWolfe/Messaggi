package com.messaggi.domain;

// Generated Oct 2, 2013 11:12:51 AM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * Device generated by hbm2java
 */
public class Device implements java.io.Serializable
{

    private long id;

    private boolean active;

    private Set<DeviceAttribute> deviceAttributes = new HashSet<DeviceAttribute>(0);

    public Device()
    {
    }

    public Device(long id, boolean active)
    {
        this.id = id;
        this.active = active;
    }

    public Device(long id, boolean active, Set<DeviceAttribute> deviceAttributes)
    {
        this.id = id;
        this.active = active;
        this.deviceAttributes = deviceAttributes;
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
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
