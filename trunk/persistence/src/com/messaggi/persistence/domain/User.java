package com.messaggi.persistence.domain;

import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

public class User
{
    private UUID id;

    private String name;

    private String email;

    private String phone;

    private int phoneParsed;

    private Locale locale;

    private boolean active;

    private HashSet<Application> applications;

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
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

    public int getPhoneParsed()
    {
        return phoneParsed;
    }

    public void setPhoneParsed(int phoneParsed)
    {
        this.phoneParsed = phoneParsed;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
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
}

