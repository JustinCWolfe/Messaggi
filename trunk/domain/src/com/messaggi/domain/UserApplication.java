package com.messaggi.domain;

// Generated Oct 8, 2013 1:37:50 PM by Hibernate Tools 4.0.0

/**
 * UserApplication generated by hbm2java
 */
public class UserApplication implements java.io.Serializable
{

    private long applicationId;

    private User user;

    public UserApplication()
    {
    }

    public UserApplication(long applicationId, User user)
    {
        this.applicationId = applicationId;
        this.user = user;
    }

    public long getApplicationId()
    {
        return this.applicationId;
    }

    public void setApplicationId(long applicationId)
    {
        this.applicationId = applicationId;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

}
