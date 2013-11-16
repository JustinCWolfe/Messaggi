package com.messaggi.persistence.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformAttributeKey
{
    private String key;

    private String description;

    private HashSet<ApplicationPlatformAttribute> applicationPlatformAttributes;

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

    public HashSet<ApplicationPlatformAttribute> getApplicationPlatformAttributes()
    {
        return applicationPlatformAttributes;
    }

    public void setApplicationPlatformAttributes(HashSet<ApplicationPlatformAttribute> applicationPlatformAttributes)
    {
        this.applicationPlatformAttributes = applicationPlatformAttributes;
    }

    public ApplicationPlatformAttributeKey()
    {

    }
}
