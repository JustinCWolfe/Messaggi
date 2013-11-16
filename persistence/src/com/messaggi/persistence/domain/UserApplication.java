package com.messaggi.persistence.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserApplication
{
    private User user;

    private Application application;

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public UserApplication()
    {

    }
}
