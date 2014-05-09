package com.messaggi.messaging.external.message;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.external.message.SendMessageResponse;

@XmlRootElement
public class AndroidSendMessageResponse extends SendMessageResponse
{
    public Response response;

    /**
     * Unique ID (number) identifying the multicast message.
     */
    @XmlElement(name = "multicast_id")
    public long multicastId;

    /**
     * Number of messages that were processed without an error.
     */
    @XmlElement(name = "success")
    public long successfulMessageCount;

    /**
     * Number of messages that could not be processed.
     */
    @XmlElement(name = "failure")
    public long failedMessageCount;

    /**
     * Number of results that contain a canonical registration ID. See Advanced
     * Topics for more discussion of this topic.
     */
    @XmlElement(name = "canonical_ids")
    public long canonicalRegistrationIdCount;

    /**
     * Array of objects representing the status of the messages processed. The
     * objects are listed in the same order as the request (i.e., for each
     * registration ID in the request, its result is listed in the same index in
     * the response).
     */
    @XmlElement(name = "results")
    public AndroidResult[] results;

    public AndroidSendMessageResponse()
    {
    }

    public AndroidSendMessageResponse(Response response)
    {
        this.response = response;
    }

    @XmlRootElement
    public static class AndroidResult
    {
        public enum GCMErrorMessage {
            INTERNAL_SERVER_ERROR("InternalServerError"), UNAVAILABLE("Unavailable"), INVALID_DATA_KEY("InvalidDataKey"), MESSAGE_TOO_BIG(
                    "MessageTooBig"), UNREGISTERED_DEVICE("NotRegistered"), INVALID_REGISTRATION_ID(
                    "InvalidRegistration"), MISSING_REGISTRATION_ID("MissingRegistration");

            private final String value;

            public String getValue()
            {
                return value;
            }

            private GCMErrorMessage(String value)
            {
                this.value = value;
            }
        }

        private final static Map<String, GCMErrorMessage> GCM_ERROR_MESSAGE_STRING_TO_ENUM_MAP = new HashMap<>();

        static {
            for (GCMErrorMessage gcmErrorMessageEnum : EnumSet.allOf(GCMErrorMessage.class)) {
                GCM_ERROR_MESSAGE_STRING_TO_ENUM_MAP.put(gcmErrorMessageEnum.value, gcmErrorMessageEnum);
            }
        }

        /**
         * String representing the message when it was successfully processed.
         */
        @XmlElement(name = "message_id")
        public String messageId;

        /**
         * If set, means that GCM processed the message but it has another
         * canonical registration ID for that device, so sender should replace
         * the IDs on future requests (otherwise they might be rejected). This
         * field is never set if there is an error in the request.
         */
        @XmlElement(name = "registration_id")
        public String registrationId;

        /**
         * String describing an error that occurred while processing the message
         * for that recipient. The possible values are the same as documented in
         * the above table, plus "Unavailable" (meaning GCM servers were busy
         * and could not process the message for that particular recipient, so
         * it could be retried).
         */
        @XmlElement(name = "error")
        public String error;

        @XmlTransient
        public GCMErrorMessage getGCMErrorMessage()
        {
            return GCM_ERROR_MESSAGE_STRING_TO_ENUM_MAP.get(error);
        }
    }
}
