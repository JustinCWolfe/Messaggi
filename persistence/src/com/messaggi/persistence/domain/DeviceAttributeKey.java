package com.messaggi.persistence.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceAttributeKey
{
    private String key;

    private String description;

    private HashSet<DeviceAttribute> deviceAttributes;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public HashSet<DeviceAttribute> getDeviceAttributes()
    {
        return deviceAttributes;
    }

    public void setDeviceAttributes(HashSet<DeviceAttribute> deviceAttributes)
    {
        this.deviceAttributes = deviceAttributes;
    }

    public DeviceAttributeKey()
    {

    }

    @Override
    public String toString()
    {
        return "DeviceAttributeKey [key=" + key + ", description=" + description + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof DeviceAttributeKey))
            return false;
        DeviceAttributeKey other = (DeviceAttributeKey) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}