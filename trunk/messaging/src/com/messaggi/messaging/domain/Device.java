package com.messaggi.messaging.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Device
{
    private Long id;

    private Boolean active;

    private HashSet<DeviceAttribute> deviceAttributes;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public HashSet<DeviceAttribute> getDeviceAttributes()
    {
        return deviceAttributes;
    }

    public void setDeviceAttributes(HashSet<DeviceAttribute> deviceAttributes)
    {
        this.deviceAttributes = deviceAttributes;
    }

    public Device()
    {

    }

    @Override
    public String toString()
    {
        return "Device [id=" + id + "]";
    }
}
