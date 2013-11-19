package com.messaggi.persistence.domain;

import java.util.HashSet;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatform
{
    private Long id;

    private UUID token;

    private Application application;

    private Platform platform;

    private HashSet<ApplicationPlatformAttribute> attributes;

    private HashSet<Device> devices;

    private HashSet<ApplicationPlatformMsgLog> msgLogs;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public UUID getToken()
    {
        return token;
    }

    public void setToken(UUID token)
    {
        this.token = token;
    }

    public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public Platform getPlatform()
    {
        return platform;
    }

    public void setPlatform(Platform platform)
    {
        this.platform = platform;
    }

    public HashSet<ApplicationPlatformAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(HashSet<ApplicationPlatformAttribute> attributes)
    {
        this.attributes = attributes;
    }

    public HashSet<Device> getDevices()
    {
        return devices;
    }

    public void setDevices(HashSet<Device> devices)
    {
        this.devices = devices;
    }

    public HashSet<ApplicationPlatformMsgLog> getMsgLogs()
    {
        return msgLogs;
    }

    public void setMsgLogs(HashSet<ApplicationPlatformMsgLog> msgLogs)
    {
        this.msgLogs = msgLogs;
    }

    public ApplicationPlatform()
    {

    }

    @Override
    public String toString()
    {
        return "ApplicationPlatform [id=" + id + "]";
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
        if (!(obj instanceof ApplicationPlatform))
            return false;
        ApplicationPlatform other = (ApplicationPlatform) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
