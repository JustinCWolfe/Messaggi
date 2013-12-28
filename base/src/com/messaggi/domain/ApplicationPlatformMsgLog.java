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

    private Integer msgCount;

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
    public Integer getApplicationPlatformId()
    {
        return (applicationPlatform != null) ? applicationPlatform.getId() : null;
    }

    @XmlAttribute(name = "Date")
    public Long getDateAsMilliseconds()
    {
        return date.getTime();
    }

    @XmlTransient
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @XmlAttribute(name = "MsgCount")
    public Integer getMsgCount()
    {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount)
    {
        this.msgCount = msgCount;
    }

    public ApplicationPlatformMsgLog()
    {
    }
}

