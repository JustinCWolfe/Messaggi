package com.messaggi.persistence.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformAttribute
{
    private ApplicationPlatform applicationPlatform;

    private ApplicationPlatformKey key;

    private String value;

    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    public ApplicationPlatformKey getKey()
    {
        return key;
    }

    public void setKey(ApplicationPlatformKey key)
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

    public ApplicationPlatformAttribute()
    {

    }
}

