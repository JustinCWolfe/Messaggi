package com.messaggi.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Device
{
    private Long id;

    private Boolean active;

    private HashSet<ApplicationPlatform> applicationPlatforms;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public HashSet<ApplicationPlatform> getApplicationPlatforms()
    {
        return applicationPlatforms;
    }

    public void setApplicationPlatforms(HashSet<ApplicationPlatform> applicationPlatforms)
    {
        this.applicationPlatforms = applicationPlatforms;
    }

    public Device()
    {

    }
}
