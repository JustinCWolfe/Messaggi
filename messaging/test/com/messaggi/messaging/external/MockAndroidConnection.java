package com.messaggi.messaging.external;

import static com.messaggi.messaging.external.AndroidConnection.AUTHORIZATION_HEADER_NAME;
import static com.messaggi.messaging.external.AndroidConnection.AUTHORIZATION_HEADER_VALUE_FORMAT;
import static com.messaggi.messaging.external.AndroidConnection.SEND_MESSAGE_WEB_TARGET;

import javax.ws.rs.client.Invocation;

import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AndroidExceptionFactory;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidMulticastException;

public class MockAndroidConnection extends AndroidConnection
{
    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        AndroidSendMessageRequest androidRequest = new AndroidSendMessageRequest(request);
        if (androidRequest.request.to.length > 1) {
            throw new AndroidMulticastException(androidRequest, null);
        }

        // Note that for android HTTP, the application platform  is not required since we do not 
        // establish a stateful connection with the messaging service.
        Invocation.Builder invocationBuilder = SEND_MESSAGE_WEB_TARGET.request(MediaType.APPLICATION_JSON_TYPE);
        String authentication = String.format(AUTHORIZATION_HEADER_VALUE_FORMAT,
                applicationPlatform.getExternalServiceToken());
        invocationBuilder.header(AUTHORIZATION_HEADER_NAME, authentication);

        //TODO: implement automatic retry using exponential back-off.
        // Errors in the 500-599 range (such as 500 or 503) indicate that there was an internal error 
        // in the GCM server while trying to process the request, or that the server is temporarily 
        // unavailable (for example, because of timeouts). Sender must retry later, honoring any 
        // Retry-After header included in the response. Application servers must implement exponential back-off.

        Entity<AndroidSendMessageRequest> entity = Entity.entity(androidRequest, MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(entity);
        // Create generic android response based on the jaxb response.
        AndroidSendMessageResponse androidResponse = new AndroidSendMessageResponse(response);
        if (Response.Status.OK.getStatusCode() == response.getStatusInfo().getStatusCode()) {
            // Create specific android response by parsing the result json entity returned by the GCM service.
            androidResponse = response.readEntity(AndroidSendMessageResponse.class);
            androidResponse.response = response;
            if (androidResponse.failedMessageCount == 0 && androidResponse.canonicalRegistrationIdCount == 0) {
                return androidResponse;
            }
        }
        throw AndroidExceptionFactory.createSendMessageException(androidRequest, androidResponse);
    }
}

