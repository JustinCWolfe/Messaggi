package com.messaggi.messages;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.messaggi.domain.Device;

public class SendMessageRequest
{
    private static final String DEFAULT_MESSAGE_KEY = "message";

    public final boolean isDebug;

    public final Device from;

    public final Date requestDate;

    public final UUID requestId;

    public final Device[] to;

    public final Map<String, String> messageMap;

    public String getDefaultMessage()
    {
        return (messageMap.containsKey(DEFAULT_MESSAGE_KEY)) ? messageMap.get(DEFAULT_MESSAGE_KEY) : null;
    }

    public SendMessageRequest(Device from, Device[] to, String messageText)
    {
        this(from, to, messageText, false);
    }

    public SendMessageRequest(Device from, Device[] to, String messageText, boolean isDebug)
    {
        this(from, to, ImmutableMap.of(DEFAULT_MESSAGE_KEY, messageText), isDebug);
    }

    public SendMessageRequest(Device from, Device[] to, Map<String, String> messageMap)
    {
        this(from, to, messageMap, false);
    }
    
    public SendMessageRequest(Device from, Device[] to, Map<String, String> messageMap, boolean isDebug)
    {
        this.from = from;
        this.to = to;
        this.messageMap = messageMap;
        this.isDebug = isDebug;
        this.requestDate = new Date();
        this.requestId = UUID.randomUUID();
    }
}

