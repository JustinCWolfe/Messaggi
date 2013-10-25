package com.messaggi.persistence.domain;

import java.util.HashSet;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatform
{
    private UUID id;

    private UUID token;

    private Application application;

    private Platform platform;

    private HashSet<ApplicationPlatformAttribute> attributes;

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
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

    public ApplicationPlatform()
    {

    }
}

