package com.messaggi.messaging.external.exception;

import com.messaggi.messages.SendMessageException;
import com.messaggi.messaging.external.AppleSendMessageRequest;
import com.messaggi.messaging.external.AppleSendMessageResponse;

public abstract class AppleSendMessageException extends SendMessageException
{
    private static final long serialVersionUID = 8701373326591668146L;

    public final AppleSendMessageRequest request;

    public final AppleSendMessageResponse response;

    public AppleSendMessageException(AppleSendMessageRequest request, AppleSendMessageResponse response)
    {
        super();
        this.request = request;
        this.response = response;
    }

    /**
     * Multicast messaging is not supported via Apple Push Notification service
     * messaging.
     */
    public static class AppleMulticastException extends AppleSendMessageException
    {
        private static final long serialVersionUID = 8175801516259880882L;

        public AppleMulticastException(AppleSendMessageRequest request, AppleSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AppleInvalidPasswordException extends AppleSendMessageException
    {
        public AppleInvalidPasswordException(AppleSendMessageRequest request, AppleSendMessageResponse response)
        {
            super(request, response);
        }
    }
}

