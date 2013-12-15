package com.messaggi.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "ApplicationPlatformMsgLog")
public class ApplicationPlatformMsgLog
{
    private Integer id;

    private ApplicationPlatform applicationPlatform;

    private Date date;

    private int msgCount;

    @XmlAttribute(name = "ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @XmlTransient
    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    @XmlAttribute(name = "ApplicationPlatformID")
    public Integer getApplicationPlatformID()
    {
        return (applicationPlatform != null) ? applicationPlatform.getId() : null;
    }

    @XmlAttribute(name = "Date")
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @XmlAttribute(name = "MsgCount")
    public int getMsgCount()
    {
        return msgCount;
    }

    public void setMsgCount(int msgCount)
    {
        this.msgCount = msgCount;
    }

    public ApplicationPlatformMsgLog()
    {
    }
}

