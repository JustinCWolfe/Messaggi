package com.messaggi.messaging.external.message.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.messaggi.external.message.exception.SendMessageException;
import com.messaggi.messaging.external.message.AndroidSendMessageRequest;
import com.messaggi.messaging.external.message.AndroidSendMessageResponse;
import com.messaggi.messaging.external.message.AndroidSendMessageResponse.AndroidResult;
import com.messaggi.messaging.external.message.AndroidSendMessageResponse.AndroidResult.GCMErrorMessage;

public abstract class AndroidSendMessageException extends SendMessageException
{
    private static final long serialVersionUID = 8343199742537841201L;

    public final AndroidSendMessageRequest request;

    public final AndroidSendMessageResponse response;

    public AndroidSendMessageException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
    {
        super();
        this.request = request;
        this.response = response;
    }

    public static class AndroidAuthenticationErrorException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = 80510491000674236L;

        public AndroidAuthenticationErrorException(AndroidSendMessageRequest request,
                AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidInvalidRegistrationIdException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = -5930352138037895624L;

        public List<String> getInvalidRegistrationIds()
        {
            List<String> invalidRegistrationIds = new ArrayList<>();
            for (int index = 0; index < response.results.length; index++) {
                AndroidResult androidResult = response.results[index];
                if (androidResult.getGCMErrorMessage() == GCMErrorMessage.INVALID_REGISTRATION_ID) {
                    invalidRegistrationIds.add(request.registrationIds[index]);
                }
            }
            return invalidRegistrationIds;
        }

        public AndroidInvalidRegistrationIdException(AndroidSendMessageRequest request,
                AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidUnregisteredDeviceException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = 2047794089543047302L;

        public List<String> getUnregisteredDeviceIds()
        {
            List<String> unregisteredDeviceIds = new ArrayList<>();
            for (int index = 0; index < response.results.length; index++) {
                AndroidResult androidResult = response.results[index];
                if (androidResult.getGCMErrorMessage() == GCMErrorMessage.UNREGISTERED_DEVICE) {
                    unregisteredDeviceIds.add(request.registrationIds[index]);
                }
            }
            return unregisteredDeviceIds;
        }

        public AndroidUnregisteredDeviceException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidInvalidDataKeyException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = 2730766572455983328L;

        public AndroidInvalidDataKeyException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidMessageTooBigException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = -3201677609737458124L;

        public AndroidMessageTooBigException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidCanonicalIdException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = 7418578416372072789L;

        public Map<String, String> getOriginalToCanonicalRegistrationIdMap()
        {
            Map<String, String> originalToCanonicalRegistrationIds = new HashMap<>();
            for (int index = 0; index < response.results.length; index++) {
                AndroidResult androidResult = response.results[index];
                if (!Strings.isNullOrEmpty(androidResult.registrationId)) {
                    originalToCanonicalRegistrationIds
                            .put(request.registrationIds[index], androidResult.registrationId);
                }
            }
            return originalToCanonicalRegistrationIds;
        }

        public AndroidCanonicalIdException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidMissingRegistrationIdException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = 2564302522072966755L;

        public AndroidMissingRegistrationIdException(AndroidSendMessageRequest request,
                AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidInternalServerErrorException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = -6057957132117821332L;

        public AndroidInternalServerErrorException(AndroidSendMessageRequest request,
                AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    public static class AndroidTimeoutException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = -1481238971734703723L;

        public AndroidTimeoutException(AndroidSendMessageRequest request, AndroidSendMessageResponse response) {
            super(request, response);
        }
    }

    public static class AndroidUnknownException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = -3900551551236788838L;

        public AndroidUnknownException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }

    /**
     * Multicast messaging is not supported via HTTP GCM messaging.
     */
    public static class AndroidMulticastException extends AndroidSendMessageException
    {
        private static final long serialVersionUID = -8582111525137460181L;

        public AndroidMulticastException(AndroidSendMessageRequest request, AndroidSendMessageResponse response)
        {
            super(request, response);
        }
    }
}

