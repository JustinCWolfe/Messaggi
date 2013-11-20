package com.messaggi.web.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformAttribute
{
    private ApplicationPlatform applicationPlatform;

    private String key;

    private String value;

    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
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

    public ApplicationPlatformAttribute()
    {

    }

    @Override
    public String toString()
    {
        return "ApplicationPlatformAttribute [applicationPlatform=" + applicationPlatform + ", key=" + key +
                ", value=" + value + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicationPlatform == null) ? 0 : applicationPlatform.hashCode());
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
        if (!(obj instanceof ApplicationPlatformAttribute))
            return false;
        ApplicationPlatformAttribute other = (ApplicationPlatformAttribute) obj;
        if (applicationPlatform == null) {
            if (other.applicationPlatform != null)
                return false;
        } else if (!applicationPlatform.equals(other.applicationPlatform))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}

