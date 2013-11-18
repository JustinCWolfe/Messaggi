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
}