package com.messaggi.domain;

import java.util.HashSet;
import java.util.Locale;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User
{
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String passwordHash;

    private String passwordSalt;

    private Locale locale;

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt()
    {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt)
    {
        this.passwordSalt = passwordSalt;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
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
    
    public User ()
    {

    }
}

