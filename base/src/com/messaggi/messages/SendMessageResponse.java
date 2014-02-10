package com.messaggi.messages;

import javax.ws.rs.core.Response;

public class SendMessageResponse
{
    public final Response response;

    public SendMessageResponse(Response response)
    {
        this.response = response;
    }
}

