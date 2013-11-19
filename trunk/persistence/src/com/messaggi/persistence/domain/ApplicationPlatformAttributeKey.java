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

    @Override
    public String toString()
    {
        return "ApplicationPlatformAttributeKey [key=" + key + ", description=" + description + "]";
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
        if (!(obj instanceof ApplicationPlatformAttributeKey))
            return false;
        ApplicationPlatformAttributeKey other = (ApplicationPlatformAttributeKey) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}
