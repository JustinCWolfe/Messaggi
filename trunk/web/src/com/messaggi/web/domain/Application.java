package com.messaggi.web.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Application
{
    private Long id;

    private String name;

    private Boolean active;

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
}

