package com.messaggi.domain;

import java.util.HashSet;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "ApplicationPlatform")
public class ApplicationPlatform
{
    public enum Platform {
        IOS("IOS"), ANDROID("ANDROID"), WINDOWS("WINDOWS");
        
        private final String value;
        
        public String getValue()
        {
            return value;
        }

        private Platform (String value) {
            this.value = value;
        }
    }

    private Integer id;

    private Application application;

    private Platform platform;

    private UUID token;

    private HashSet<Device> devices;

    private HashSet<ApplicationPlatformMsgLog> applicationPlatformMsgLogs;

    @XmlAttribute(name = "ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @XmlTransient
    public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    @XmlAttribute(name = "ApplicationID")
    public Integer getApplicationId()
    {
        return (application != null) ? application.getId() : null;
    }

    @XmlTransient
    public Platform getPlatform()
    {
        return platform;
    }

    public void setPlatform(Platform platform)
    {
        this.platform = platform;
    }

    @XmlAttribute(name = "PlatformCode")
    public String getPlatformCode()
    {
        return (platform != null) ? platform.value : null;
    }

    @XmlAttribute(name = "Token")
    public UUID getToken()
    {
        return token;
    }

    public void setToken(UUID token)
    {
        this.token = token;
    }

    @XmlTransient
    public HashSet<Device> getDevices()
    {
        return devices;
    }

    public void setDevices(HashSet<Device> devices)
    {
        this.devices = devices;
    }

    @XmlTransient
    public HashSet<ApplicationPlatformMsgLog> getApplicationPlatformMsgLogs()
    {
        return applicationPlatformMsgLogs;
    }

    public void setApplicationPlatformMsgLogs(HashSet<ApplicationPlatformMsgLog> applicationPlatformMsgLogs)
    {
        this.applicationPlatformMsgLogs = applicationPlatformMsgLogs;
    }

    public ApplicationPlatform()
    {

    }
}