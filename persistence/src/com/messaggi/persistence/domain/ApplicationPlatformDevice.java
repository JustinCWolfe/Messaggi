package com.messaggi.persistence.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformDevice
{
    private ApplicationPlatform applicationPlatform;

    private Device device;

    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }

    public ApplicationPlatformDevice()
    {

    }

    @Override
    public String toString()
    {
        return "ApplicationPlatformDevice [applicationPlatform=" + applicationPlatform + ", device=" + device + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicationPlatform == null) ? 0 : applicationPlatform.hashCode());
        result = prime * result + ((device == null) ? 0 : device.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ApplicationPlatformDevice))
            return false;
        ApplicationPlatformDevice other = (ApplicationPlatformDevice) obj;
        if (applicationPlatform == null) {
            if (other.applicationPlatform != null)
                return false;
        } else if (!applicationPlatform.equals(other.applicationPlatform))
            return false;
        if (device == null) {
            if (other.device != null)
                return false;
        } else if (!device.equals(other.device))
            return false;
        return true;
    }
}
