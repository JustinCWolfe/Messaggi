package com.messaggi.messaging.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceAttribute
{
    private Device device;

    private String key;

    private String value;

    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
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

    @Override
    public String toString()
    {
        return "DeviceAttribute [device=" + device + ", key=" + key + ", value=" + value + "]";
    }
}
