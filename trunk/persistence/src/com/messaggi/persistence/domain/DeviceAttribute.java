package com.messaggi.persistence.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceAttribute
{
    private Device device;

    private DeviceAttributeKey key;

    private String value;

    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }

    public DeviceAttributeKey getKey()
    {
        return key;
    }

    public void setKey(DeviceAttributeKey key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public DeviceAttribute()
    {

    }
}
