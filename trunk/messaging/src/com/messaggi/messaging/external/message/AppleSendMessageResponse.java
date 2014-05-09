package com.messaggi.messaging.external.message;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import com.messaggi.external.message.SendMessageResponse;

@XmlRootElement
public class AppleSendMessageResponse extends SendMessageResponse
{
    public Response response;

    public AppleSendMessageResponse()
    {
    }

    public AppleSendMessageResponse(Response response)
    {
        this.response = response;
    }
}

