package com.messaggi.persistence.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Application
{
    private Long id;

    private String name;

    private Boolean active;

    private HashSet<ApplicationPlatform> applicationPlatforms;

    private User user;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Application()
    {

    }

    @Override
    public String toString()
    {
        return "Application [id=" + id + "]";
    }
}

