package com.messaggi.messages;

import com.messaggi.domain.Device;

public class SendMessageRequest
{
    public final Device from;

    public final Device to;

    public final Message msg;

    public SendMessageRequest(Device from, Device to, Message msg)
    {
        this.from = from;
        this.to = to;
        this.msg = msg;
    }
}

