package com.messaggi.messaging.external.connection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.SendMessageResponse;
import com.messaggi.external.message.exception.SendMessageException;
import com.messaggi.messaging.external.message.AndroidSendMessageRequest;
import com.messaggi.messaging.external.message.AndroidSendMessageResponse;
import com.messaggi.messaging.external.message.exception.AndroidExceptionFactory;
import com.messaggi.messaging.external.message.exception.AndroidSendMessageException.AndroidMulticastException;

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

    /**
     * Connections are only needed when using the asynchronous Cloud Connection
     * Service (CCS) XMPP messaging service. Standard HTTP messaging is
     * stateless so no connections need be disconnected.
     */
    @Override
    public void disconnect() throws Exception
    {

    }

    protected Response sendMessageInternal(AndroidSendMessageRequest androidRequest)
    {
        // Note that for android HTTP, the application platform  is not required since we do not 
        // establish a stateful connection with the messaging service.
        Invocation.Builder invocationBuilder = SEND_MESSAGE_WEB_TARGET.request(MediaType.APPLICATION_JSON_TYPE);
        String authentication = String.format(AUTHORIZATION_HEADER_VALUE_FORMAT,
                applicationPlatform.getExternalServiceToken());
        invocationBuilder.header(AUTHORIZATION_HEADER_NAME, authentication);
        Entity<AndroidSendMessageRequest> entity = Entity.entity(androidRequest, MediaType.APPLICATION_JSON_TYPE);
        return invocationBuilder.post(entity);
    }

    protected AndroidSendMessageResponse getEntityFromResponse(Response response)
    {
        return response.readEntity(AndroidSendMessageResponse.class);
    }

    protected SendMessageResponse processSendMessageResponse(AndroidSendMessageRequest androidRequest, Response response)
        throws SendMessageException
    {
        // Create generic android response based on the jaxb response.
        AndroidSendMessageResponse androidResponse = new AndroidSendMessageResponse(response);
        if (Response.Status.OK.getStatusCode() == response.getStatusInfo().getStatusCode()) {
            // Create specific android response by parsing the result json entity returned by the GCM service.
            androidResponse = getEntityFromResponse(response);
            androidResponse.response = response;
            if (androidResponse.failedMessageCount == 0 && androidResponse.canonicalRegistrationIdCount == 0) {
                return androidResponse;
            }
        }
        throw AndroidExceptionFactory.createSendMessageException(androidRequest, androidResponse);
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        AndroidSendMessageRequest androidRequest = new AndroidSendMessageRequest(request);
        if (androidRequest.request.to.length > 1) {
            throw new AndroidMulticastException(androidRequest, null);
        }
        Response response = sendMessageInternal(androidRequest);
        return processSendMessageResponse(androidRequest, response);
    }
}

