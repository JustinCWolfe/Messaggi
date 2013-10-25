package com.messaggi.persistence.domain;

import java.util.HashSet;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Platform
{
    public enum PlatformServiceName {
        iOS, Android
    }

    private UUID id;

    private String name;

    private PlatformServiceName serviceName;

    private boolean active;

    private HashSet<Application> applications;

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public PlatformServiceName getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(PlatformServiceName serviceName)
    {
        this.serviceName = serviceName;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public HashSet<Application> getApplications()
    {
        return applications;
    }

    public void setApplications(HashSet<Application> applications)
    {
        this.applications = applications;
    }

    public Platform()
    {

    }
}

