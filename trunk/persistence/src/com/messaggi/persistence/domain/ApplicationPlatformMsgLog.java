package com.messaggi.persistence.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformMsgLog
{
    private Long id;

    private Date date;

    private Integer msgCount;

    private ApplicationPlatform applicationPlatform;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Integer getMsgCount()
    {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount)
    {
        this.msgCount = msgCount;
    }

    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    public ApplicationPlatformMsgLog()
    {

    }
}