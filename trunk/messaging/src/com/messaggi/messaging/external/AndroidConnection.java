package com.messaggi.messaging.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.glassfish.jersey.client.ClientConfig;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.util.JAXBHelper;

public class AndroidConnection implements MessagingServiceConnection
{
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private static final String AUTHORIZATION_HEADER_VALUE_FORMAT = "key=%s";

    private static final ClientConfig CLIENT_CONFIG = new ClientConfig();

    private static final Client CLIENT = ClientBuilder.newClient(CLIENT_CONFIG);

    private static final WebTarget SEND_MESSAGE_WEB_TARGET = CLIENT.target("https://android.googleapis.com/gcm/send");

    private ApplicationPlatform applicationPlatform;

    @Override
    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    @Override
    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    /**
     * Connections are only needed when using the asynchronous Cloud Connection
     * Service (CCS) XMPP messaging service. Standard HTTP messaging is
     * stateless so no connections need be established.
     */
    @Override
    public void connect() throws Exception
    {

    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        // Note that for android HTTP, the application platform  is not required since we do not 
        // establish a stateful connection with the messaging service.
        Invocation.Builder invocationBuilder = SEND_MESSAGE_WEB_TARGET.request();
        String authentication = String.format(AUTHORIZATION_HEADER_VALUE_FORMAT,
                applicationPlatform.getExternalServiceToken());
        invocationBuilder.header(AUTHORIZATION_HEADER_NAME, authentication);
        
        AndroidSendMessageRequest androidRequest = new AndroidSendMessageRequest(request);
        if (request.isDebug) {
            try {
                System.out.println(JAXBHelper.objectToXML(androidRequest));
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        Entity<AndroidSendMessageRequest> entity = Entity.entity(androidRequest , MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(entity);
        if (Response.Status.OK.getStatusCode() != response.getStatusInfo().getStatusCode()) {
            throw new AndroidSendMessageException(response);
        }
        return new AndroidSendMessageResponse(response);
    }

    @XmlRootElement(name = "")
    static class AndroidSendMessageRequest
    {
        private SendMessageRequest request;

        /**
         * A string array with the list of devices (registration IDs) receiving
         * the message. It must contain at least 1 and at most 1000 registration
         * IDs. Note that a regID represents a particular Android application
         * running on a particular device.
         * 
         * @return
         */
        @XmlElement(name = "registration_ids")
        public String[] getRegistrationIds()
        {
            List<String> recipients = new ArrayList<>();
            for (Device toDevice : request.to) {
                recipients.add(toDevice.getCode());
            }
            return recipients.toArray(new String[recipients.size()]);
        }

        /**
         * A string that maps a single user to multiple registration IDs
         * associated with that user. This allows a 3rd-party server to send a
         * single message to multiple app instances (typically on multiple
         * devices) owned by a single user. A 3rd-party server can use
         * notification_key as the target for a message instead of an individual
         * registration ID (or array of registration IDs). The maximum number of
         * members allowed for a notification_key is 10.
         * 
         * @return
         */
        @XmlElement(name = "notification_key")
        public String getNotificationKey()
        {
            return null;
        }

        /**
         * A JSON object whose fields represents the key-value pairs of the
         * message's payload data. If present, the payload data it will be
         * included in the Intent as application data, with the key being the
         * extra's name. For instance, "data":{"score":"3x1"} would result in an
         * intent extra named score whose value is the string 3x1. There is no
         * limit on the number of key/value pairs, though there is a limit on
         * the total size of the message (4kb). The values could be any JSON
         * object, but we recommend using strings, since the values will be
         * converted to strings in the GCM server anyway.
         * 
         * @return
         */
        //@XmlElement(name = "data")
        @XmlTransient
        public Map<String, String> getData()
        {
            return request.messageMap;
        }

        /**
         * A string containing the package name of your application. When set,
         * messages will only be sent to registration IDs that match the package
         * name.
         * 
         * @return
         */
        @XmlElement(name = "restricted_package_name")
        public String getRestrictedPackageName()
        {
            return null;
        }

        /**
         * If included, allows developers to test their request without actually
         * sending a message. Optional. The default value is false, and must be
         * a JSON boolean.
         * 
         * @return
         */
        @XmlElement(name = "dry_run")
        public boolean getDryRun()
        {
            return request.isDebug;
        }

        public AndroidSendMessageRequest()
        {
        }

        public AndroidSendMessageRequest(SendMessageRequest request)
        {
            this.request = request;
        }
    }
    
    @XmlRootElement
    static class AndroidSendMessageResponse extends SendMessageResponse
    {
	    public final Response response;
	
        //multicast_id    Unique ID (number) identifying the multicast message.
        //success Number of messages that were processed without an error.
        //failure Number of messages that could not be processed.
        //canonical_ids   Number of results that contain a canonical registration ID. See Advanced Topics for more discussion of this topic.
        //results Array of objects representing the status of the messages processed. The objects are listed in the same order as the request (i.e., for each registration ID in the request, its result is listed in the same index in the response) and they can have these fields: message_id: String representing the message when it was successfully processed. registration_id: If set, means that GCM processed the message but it has another canonical registration ID for that device, so sender should replace the IDs on future requests (otherwise they might be rejected). This field is never set if there is an error in the request. error: String describing an error that occurred while processing the message for that recipient. The possible values are the same as documented in the above table, plus "Unavailable" (meaning GCM servers were busy and could not process the message for that particular recipient, so it could be retried). 

        public AndroidSendMessageResponse(Response response)
	    {
	        this.response = response;
	    }
    }

    static class AndroidSendMessageException extends SendMessageException
    {
        private static final long serialVersionUID = 8343199742537841201L;
        
        public final Response response;

        public AndroidSendMessageException(Response response)
        {
            super();
            this.response = response;
        }
    }
}

