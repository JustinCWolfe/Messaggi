package com.messaggi.persistence.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformAttribute
{
    private ApplicationPlatform applicationPlatform;

    private ApplicationPlatformAttributeKey key;

    private String value;

    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    public ApplicationPlatformAttributeKey getKey()
    {
        return key;
    }

    public void setKey(ApplicationPlatformAttributeKey key)
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

    @Override
    public String toString()
    {
        return "ApplicationPlatformAttribute [applicationPlatform=" + applicationPlatform + ", key=" + key +
                ", value=" + value + "]";
    }
}

