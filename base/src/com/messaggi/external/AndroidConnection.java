package com.messaggi.external;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.client.ClientConfig;

import com.messaggi.domain.ApplicationPlatform;
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
        //Required. When your app server sends a message in GCM, it must specify a target. 
        //registration_ids: For sending to 1 or more devices (up to 1000). When you send a message to multiple registration IDs, that is called a multicast message.
        //notification_key: For sending to multiple devices owned by a single user.

        //Message parameters
        //The following table lists the parameters that a 3rd-party app server might include in the JSON messages it sends to a connection server. See the "Where Supported" column for information about which connection servers support that particular parameter.
        //Field   Description Where Supported
        //to  In CCS, used in place of registration_ids to specify the recipient of a message. Its value must be a registration ID. The value is a string. Required.  CCS
        //message_id  In CCS, uniquely identifies a message in an XMPP connection. The value is a string that uniquely identifies the associated message. The value is a string. Required.    CCS
        //message_type    In CCS, indicates a special status message, typically sent by the system. However, your app server also uses this parameter to send an 'ack' or 'nack' message back to the CCS connection server. For more discussion of this topic, see Cloud Connection Server. The value is a string. Optional.  CCS
        //registration_ids    A string array with the list of devices (registration IDs) receiving the message. It must contain at least 1 and at most 1000 registration IDs. To send a multicast message, you must use JSON. For sending a single message to a single device, you could use a JSON object with just 1 registration id, or plain text (see below). A request must include a recipient—this can be either a registration ID, an array of registration IDs, or a notification_key. Required.    HTTP
        //notification_key    A string that maps a single user to multiple registration IDs associated with that user. This allows a 3rd-party server to send a single message to multiple app instances (typically on multiple devices) owned by a single user. A 3rd-party server can usenotification_key as the target for a message instead of an individual registration ID (or array of registration IDs). The maximum number of members allowed for a notification_key is 10. For more discussion of this topic, see User Notifications. Optional. HTTP. This feature is supported in CCS, but you use it by specifying a notification key in the "to" field.
        //collapse_key    An arbitrary string (such as "Updates Available") that is used to collapse a group of like messages when the device is offline, so that only the last message gets sent to the client. This is intended to avoid sending too many messages to the phone when it comes back online. Note that since there is no guarantee of the order in which messages get sent, the "last" message may not actually be the last message sent by the application server. Collapse keys are also called send-to-sync messages. Note: GCM allows a maximum of 4 different collapse keys to be used by the GCM server at any given time. In other words, the GCM server can simultaneously store 4 different send-to-sync messages per device, each with a different collapse key. If you exceed this number GCM will only keep 4 collapse keys, with no guarantees about which ones they will be. SeeAdvanced Topics for more discussion of this topic. Optional.   CCS, HTTP
        //data    A JSON object whose fields represents the key-value pairs of the message's payload data. If present, the payload data it will be included in the Intent as application data, with the key being the extra's name. For instance, "data":{"score":"3x1"} would result in an intent extra named score whose value is the string 3x1. There is no limit on the number of key/value pairs, though there is a limit on the total size of the message (4kb). The values could be any JSON object, but we recommend using strings, since the values will be converted to strings in the GCM server anyway. If you want to include objects or other non-string data types (such as integers or booleans), you have to do the conversion to string yourself. Also note that the key cannot be a reserved word (from or any word starting with google.). To complicate things slightly, there are some reserved words (such as collapse_key) that are technically allowed in payload data. However, if the request also contains the word, the value in the request will overwrite the value in the payload data. Hence using words that are defined as field names in this table is not recommended, even in cases where they are technically allowed. Optional.  CCS, HTTP
        //delay_while_idle    If included, indicates that the message should not be sent immediately if the device is idle. The server will wait for the device to become active, and then only the last message for each collapse_key value will be sent. The default value is false, and must be a JSON boolean. Optional.  CCS, HTTP
        //time_to_live    How long (in seconds) the message should be kept on GCM storage if the device is offline. Optional (default time-to-live is 4 weeks, and must be set as a JSON number). CCS, HTTP
        //restricted_package_name A string containing the package name of your application. When set, messages will only be sent to registration IDs that match the package name. Optional.   HTTP
        //dry_run If included, allows developers to test their request without actually sending a message. Optional. The default value is false, and must be a JSON boolean.  HTTP

        //If you want to test your request (either JSON or plain text) without delivering the message to the devices, you can set an optional HTTP or JSON parameter called dry_run with the value true. The result will be almost identical to running the request without this parameter, except that the message will not be delivered to the devices. Consequently, the response will contain fake IDs for the message and multicast fields.

        //Content-Type:application/json
        //Authorization:key=AIzaSyB-1uEai2WiUapxCs2Q0GZYzPu7Udno5aA
        //{
        //  "registration_ids" : ["APA91bHun4MxP5egoKMwt2KZFBaFUH-1RYqx..."],
        //  "data" : {
        //    ...
        //  },
        //}

        Invocation.Builder invocationBuilder = SEND_MESSAGE_WEB_TARGET.request(MediaType.APPLICATION_JSON_TYPE);
        invocationBuilder.header(AUTHORIZATION_HEADER_NAME,
                String.format(AUTHORIZATION_HEADER_VALUE_FORMAT, applicationPlatform.getExternalServiceToken()));
        AndroidSendMessageRequest androidRequest = new AndroidSendMessageRequest(request);
        Entity<AndroidSendMessageRequest> entity = Entity.entity(androidRequest, MediaType.APPLICATION_JSON_TYPE);
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

        @XmlElement(name = "registration_ids")
        public String[] getRegistrationIds()
        {
            return null;
        }

        AndroidSendMessageRequest(SendMessageRequest request)
        {
            this.request = request;
        }
    }
}

