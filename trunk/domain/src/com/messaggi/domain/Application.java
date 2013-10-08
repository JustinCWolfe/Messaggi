package com.messaggi.domain;

// Generated Oct 8, 2013 1:37:50 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * Application generated by hbm2java
 */
public class Application implements java.io.Serializable
{

    private long id;

    private boolean active;

    private Set<ApplicationPlatform> applicationPlatforms = new HashSet<ApplicationPlatform>(0);

    private UserApplication userApplication;

    public Application()
    {
    }

    public Application(long id, boolean active)
    {
        this.id = id;
        this.active = active;
    }

    public Application(long id, boolean active, Set<ApplicationPlatform> applicationPlatforms,
            UserApplication userApplication)
    {
        this.id = id;
        this.active = active;
        this.applicationPlatforms = applicationPlatforms;
        this.userApplication = userApplication;
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public Set<ApplicationPlatform> getApplicationPlatforms()
    {
        return this.applicationPlatforms;
    }

    public void setApplicationPlatforms(Set<ApplicationPlatform> applicationPlatforms)
    {
        this.applicationPlatforms = applicationPlatforms;
    }

    public UserApplication getUserApplication()
    {
        return this.userApplication;
    }

    public void setUserApplication(UserApplication userApplication)
    {
        this.userApplication = userApplication;
    }

}