package com.messaggi.persistence.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Platform
{
    public enum PlatformServiceName {
        IOS("iOS"), ANDROID("Android");
        
        private final String serviceName;
        
        public String serviceName()
        {
            return serviceName;
        }

        PlatformServiceName(String serviceName)
        {
            this.serviceName = serviceName;
        }
    }

    private Long id;

    private String name;

    private PlatformServiceName serviceName;

    private Boolean active;

    private HashSet<Application> applications;

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

    public PlatformServiceName getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(PlatformServiceName serviceName)
    {
        this.serviceName = serviceName;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
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

    @Override
    public String toString()
    {
        return "Platform [id=" + id + "]";
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
        if (!(obj instanceof Platform))
            return false;
        Platform other = (Platform) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}

