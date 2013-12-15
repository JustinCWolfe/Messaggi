package com.messaggi.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Application")
public class Application
{
    private Integer id;

    private String name;

    private Boolean active;

    private User user;

    @XmlAttribute(name = "ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "Name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @XmlAttribute(name = "UserID")
    public Integer getUserId()
    {
        return (user != null) ? user.getId() : null;
    }

    public Application()
    {

    }
}

