package com.messaggi.domain;

import java.util.HashSet;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.util.EncodeHelper;

@XmlRootElement(name = "User")
public class User
{
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private byte[] passwordHash;

    private String passwordSalt;

    private Locale locale;

    private Boolean active = true;

    private HashSet<Application> applications;

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

    @XmlAttribute(name = "Email")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @XmlAttribute(name = "Phone")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @XmlTransient
    public String getPasswordHash()
    {
        return (passwordHash != null) ? EncodeHelper.decodeBase64Image(passwordHash) : null;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = (passwordHash != null) ? EncodeHelper.encodeBase64Image(passwordHash) : null;
    }

    @XmlAttribute(name = "PasswordHash")
    public byte[] getPasswordHashAsBinary()
    {
        return passwordHash;
    }

    public void setPasswordHashAsBinary(byte[] passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    @XmlAttribute(name = "PasswordSalt")
    public String getPasswordSalt()
    {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt)
    {
        this.passwordSalt = passwordSalt;
    }

    @XmlAttribute(name = "Locale")
    public String getLocaleAsLanguageTag()
    {
        return locale.toLanguageTag();
    }

    @XmlTransient
    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
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

