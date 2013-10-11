package com.messaggi.domain;

// Generated Oct 11, 2013 3:07:40 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DeviceKey generated by hbm2java
 */
@Entity
@Table(name = "device_key", schema = "public")
public class DeviceKey implements java.io.Serializable
{

    private String key;

    private String description;

    private Set<DeviceAttribute> deviceAttributes = new HashSet<DeviceAttribute>(0);

    public DeviceKey()
    {
    }

    public DeviceKey(String key, String description)
    {
        this.key = key;
        this.description = description;
    }

    public DeviceKey(String key, String description, Set<DeviceAttribute> deviceAttributes)
    {
        this.key = key;
        this.description = description;
        this.deviceAttributes = deviceAttributes;
    }

    @Id
    @Column(name = "key", unique = true, nullable = false, length = 20)
    public String getKey()
    {
        return this.key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @Column(name = "description", nullable = false, length = 1024)
    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deviceKey")
    public Set<DeviceAttribute> getDeviceAttributes()
    {
        return this.deviceAttributes;
    }

    public void setDeviceAttributes(Set<DeviceAttribute> deviceAttributes)
    {
        this.deviceAttributes = deviceAttributes;
    }

}
