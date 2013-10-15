package com.messaggi.persistence.domain;

// Generated Oct 15, 2013 1:23:14 PM by Hibernate Tools 4.0.0

/**
 * ApplicationPlatformAttributeId generated by hbm2java
 */
public class ApplicationPlatformAttributeId implements java.io.Serializable
{

    private long applicationPlatformId;

    private String key;

    public ApplicationPlatformAttributeId()
    {
    }

    public ApplicationPlatformAttributeId(long applicationPlatformId, String key)
    {
        this.applicationPlatformId = applicationPlatformId;
        this.key = key;
    }

    public long getApplicationPlatformId()
    {
        return this.applicationPlatformId;
    }

    public void setApplicationPlatformId(long applicationPlatformId)
    {
        this.applicationPlatformId = applicationPlatformId;
    }

    public String getKey()
    {
        return this.key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public boolean equals(Object other)
    {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof ApplicationPlatformAttributeId))
            return false;
        ApplicationPlatformAttributeId castOther = (ApplicationPlatformAttributeId) other;

        return (this.getApplicationPlatformId() == castOther.getApplicationPlatformId()) &&
                ((this.getKey() == castOther.getKey()) || (this.getKey() != null && castOther.getKey() != null && this
                        .getKey().equals(castOther.getKey())));
    }

    public int hashCode()
    {
        int result = 17;

        result = 37 * result + (int) this.getApplicationPlatformId();
        result = 37 * result + (getKey() == null ? 0 : this.getKey().hashCode());
        return result;
    }

}
