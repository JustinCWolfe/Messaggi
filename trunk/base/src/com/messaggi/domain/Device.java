package com.messaggi.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Device")
public class Device
{
    private String code;

    private Boolean active = true;

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

    @XmlTransient
    public HashSet<ApplicationPlatform> getApplicationPlatforms()
    {
        return applicationPlatforms;
    }

    public void setApplicationPlatforms(HashSet<ApplicationPlatform> applicationPlatforms)
    {
        this.applicationPlatforms = applicationPlatforms;
    }

    /**
     * This is used during the save process - at which time a device should only
     * have a reference to the application platform ID that it is currently
     * registering for. In this case we can make the set into a list and use the
     * first (and only) element.
     * 
     * @return
     */
    @XmlAttribute(name = "ApplicationPlatformID")
    public Integer getApplicationPlatformId()
    {
        boolean hasApplicationPlatform = (applicationPlatforms != null && applicationPlatforms.size() > 0);
        if (hasApplicationPlatform) {
            List<ApplicationPlatform> applicationPlatformList = new ArrayList<>(applicationPlatforms);
            if (applicationPlatformList.size() == 1) {
                return applicationPlatformList.get(0).getId();
            }
            throw new IllegalStateException();
        }
        return null;
    }

    public Device()
    {

    }
}
