package com.messaggi.domain;

import java.util.Date;

public class ApplicationPlatformMsgLog
{
    private Long id;

    private ApplicationPlatform applicationPlatform;

    private Date date;

    private int msgCount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

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

