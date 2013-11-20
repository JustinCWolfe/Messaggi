package com.messaggi.messaging.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatformMsgLog
{
    private Long id;

    private Date date;

    private Integer msgCount;

    private Long applicationPlatformId;

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

    public Long getApplicationPlatform()
    {
        return applicationPlatformId;
    }

    public void setApplicationPlatform(Long applicationPlatformId)
    {
        this.applicationPlatformId = applicationPlatformId;
    }

    public ApplicationPlatformMsgLog()
    {

    }

    @Override
    public String toString()
    {
        return "ApplicationPlatformMsgLog [id=" + id + "]";
    }
}
