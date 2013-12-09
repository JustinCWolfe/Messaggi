package com.messaggi.domain;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Device")
public class Device
{
    private String code;

    private Boolean active;

    private HashSet<ApplicationPlatform> applicationPlatforms;

    @XmlAttribute(name = "Code")
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @XmlAttribute(name = "Active")
    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    @XmlAttribute(name = "ApplicationPlatforms")
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
