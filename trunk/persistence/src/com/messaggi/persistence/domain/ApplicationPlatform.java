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
}
