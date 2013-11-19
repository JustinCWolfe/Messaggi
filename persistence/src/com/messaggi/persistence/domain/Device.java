package com.messaggi.persistence.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Device
{
    private Long id;

    private Boolean active;

    private HashSet<DeviceAttribute> deviceAttributes;

    private HashSet<ApplicationPlatform> applicationPlatforms;

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

    public HashSet<ApplicationPlatform> getApplicationPlatforms()
    {
        return applicationPlatforms;
    }

    public void setApplicationPlatforms(HashSet<ApplicationPlatform> applicationPlatforms)
    {
        this.applicationPlatforms = applicationPlatforms;
    }

    public Device()
    {

    }

    @Override
    public String toString()
    {
        return "Device [id=" + id + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Device))
            return false;
        Device other = (Device) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}