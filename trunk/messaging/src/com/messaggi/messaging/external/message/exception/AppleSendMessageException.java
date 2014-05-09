package com.messaggi.messaging.external.message.exception;

import com.messaggi.external.message.exception.SendMessageException;
import com.messaggi.messaging.external.message.AppleSendMessageRequest;
import com.messaggi.messaging.external.message.AppleSendMessageResponse;

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

    public AppleSendMessageException(AppleSendMessageRequest request, AppleSendMessageResponse response, Throwable cause)
    {
        super(cause);
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

    public static class AppleNotConnectedException extends AppleSendMessageException
    {
        private static final long serialVersionUID = 7020492528145170347L;

        public AppleNotConnectedException(AppleSendMessageRequest request, AppleSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AppleInvalidPayloadException extends AppleSendMessageException
    {
        private static final long serialVersionUID = 2926278104410896176L;

        public AppleInvalidPayloadException(AppleSendMessageRequest request, AppleSendMessageResponse response)
        {
            super(request, response);
        }

        public AppleInvalidPayloadException(AppleSendMessageRequest request, AppleSendMessageResponse response,
                Throwable cause)
        {
            super(request, response, cause);
        }
    }
}

