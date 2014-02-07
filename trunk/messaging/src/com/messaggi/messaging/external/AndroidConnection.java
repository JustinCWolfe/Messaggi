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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.client.ClientConfig;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;

public class AndroidConnection implements MessagingServiceConnection
{
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization:";

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
    public SendMessageResponse sendMessage(SendMessageRequest request)
    {
        // Note that for android HTTP, the application platform  is not required since we do not 
        // establish a stateful connection with the messaging service.
        Invocation.Builder invocationBuilder = SEND_MESSAGE_WEB_TARGET.request();
        //invocationBuilder.header(AUTHORIZATION_HEADER_NAME,
        //        String.format(AUTHORIZATION_HEADER_VALUE_FORMAT, applicationPlatform.getExternalServiceToken()));
        Entity<AndroidSendMessageRequest> entity = Entity.entity(new AndroidSendMessageRequest(request),
                MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(entity);
        System.out.println(response.getStatus());
        //assertEquals(Response.Status.OK.getFamily(), response.getStatusInfo().getFamily());
        //assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        SendMessageResponse smr = response.readEntity(SendMessageResponse.class);

        //When the message is processed successfully, the HTTP response has a 200 status and the body contains more information about the status of the message (including possible errors). When the request is rejected, the HTTP response contains a non-200 status code (such as 400, 401, or 503).
        //The following table summarizes the statuses that the HTTP response header might contain. Click the troubleshoot link for advice on how to deal with each type of error.
        //Response    Description
        //200 Message was processed successfully. The response body will contain more details about the message status, but its format will depend whether the request was JSON or plain text. SeeInterpreting a success response for more details.
        //400 Only applies for JSON requests. Indicates that the request could not be parsed as JSON, or it contained invalid fields (for instance, passing a string where a number was expected). The exact failure reason is described in the response and the problem should be addressed before the request can be retried.
        //401 There was an error authenticating the sender account. Troubleshoot
        //5xx Errors in the 500-599 range (such as 500 or 503) indicate that there wa an internal error in the GCM server while trying to process the request, or that the server is temporarily unavailable (for example, because of timeouts). Sender must retry later, honoring any Retry-Afterheader included in the response. Application servers must implement exponential back-off.Troubleshoot

        return smr;
    }

    @XmlRootElement
    private class AndroidSendMessageRequest
    {
        private final SendMessageRequest request;

        /**
         * A string array with the list of devices (registration IDs) receiving
         * the message. It must contain at least 1 and at most 1000 registration
         * IDs. Note that a regID represents a particular Android application
         * running on a particular device.
         * 
         * @return
         */
        @XmlAttribute(name = "registration_ids")
        public String[] getRegistrationIds()
        {
            List<String> recipients = new ArrayList<>();
            for (Device toDevice : request.to) {
                recipients.add(toDevice.getCode());
            }
            return recipients.toArray(new String[recipients.size()]);
        }

        public void setRegistrationIds(String[] ids)
        {

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
        @XmlAttribute(name = "notification_key")
        public String getNotificationKey()
        {
            return null;
        }

        public void setNotificationKey(String key)
        {

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
        @XmlAttribute(name = "data")
        public Map<String, String> getData()
        {
            return request.messageMap;
        }

        public void setData(Map<String, String> data)
        {

        }

        /**
         * A string containing the package name of your application. When set,
         * messages will only be sent to registration IDs that match the package
         * name.
         * 
         * @return
         */
        @XmlAttribute(name = "restricted_package_name")
        public String getRestrictedPackageName()
        {
            return null;
        }

        public void setRetrictedPackageName(String name)
        {

        }

        /**
         * If included, allows developers to test their request without actually
         * sending a message. Optional. The default value is false, and must be
         * a JSON boolean.
         * 
         * @return
         */
        @XmlAttribute(name = "dry_run")
        public boolean getDryRun()
        {
            return request.isDebug;
        }

        public void isDryRun(boolean isDryRun)
        {

        }

        public AndroidSendMessageRequest(SendMessageRequest request)
        {
            this.request = request;
        }
    }
}

