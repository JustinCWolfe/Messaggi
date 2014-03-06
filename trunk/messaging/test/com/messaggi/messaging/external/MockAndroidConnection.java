package com.messaggi.messaging.external;

import java.util.Random;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.RandomStringUtils;

import com.messaggi.messaging.external.AndroidSendMessageResponse.AndroidResult;
import com.messaggi.messaging.external.AndroidSendMessageResponse.AndroidResult.GCMErrorMessage;

public class MockAndroidConnection extends AndroidConnection
{
    enum ResponseType {
        INTERNAL_SERVER_ERROR_FROM_RESULT, // The request status is ok but the server error is signaled in the result.
        INTERNAL_SERVER_ERROR_FROM_STATUS, // The request status is internal server error.
        SERVER_TIMEOUT_FROM_RESULT, // The request status is ok but the timeout is signaled in the result.
        SERVER_TIMEOUT_FROM_STATUS, // The request status is between 500 and 600.
        UNREGISTERED_DEVICE, INVALID_DATA_KEY, NEW_CANONICAL_ID, UNKNOWN_ERROR, MISSING_REGISTRATION_ID,
    };

    // For testing purposes, set the response status on the AndroidUnknownException to no-content.
    public static final Status UNKNOWN_ERROR_STATUS = Response.Status.NO_CONTENT;

    private final ResponseType responseType;

    private static final Random random = new Random();

    private static AndroidSendMessageResponse getEmptyAndroidSendMessageResponse()
    {
        AndroidSendMessageResponse androidResponse = new AndroidSendMessageResponse();
        androidResponse.canonicalRegistrationIdCount = 0;
        androidResponse.failedMessageCount = 1;
        // If the value in multicastId is MIN_VALUE, its absolute value cannot be mapped to 
        // a long .  To handle this case, manually compute the absolute value for MIN_VALUE
        // by changing it to MAX_VALUE.
        long multicastId = random.nextLong();
        multicastId = (multicastId == Long.MIN_VALUE) ? Long.MAX_VALUE : Math.abs(multicastId);
        androidResponse.multicastId = multicastId;
        androidResponse.successfulMessageCount = 0;
        androidResponse.results = new AndroidResult[] { new AndroidResult() };
        return androidResponse;
    }

    /**
     * If you create a response object via the ResponseBuilder (as in
     * sendMessageInternal below), it will create an OutboundJaxrsResponse
     * rather than in InboundJaxrsResponse. Jersey cannot call readEntity on an
     * OutboundJaxrsResponse (it throws: java.lang.IllegalStateException: Method
     * not supported on an outbound message. exception) so instead call
     * getEntity.
     */
    @Override
    protected AndroidSendMessageResponse getEntityFromResponse(Response response)
    {
        return (AndroidSendMessageResponse) response.getEntity();
    }

    @Override
    protected Response sendMessageInternal(AndroidSendMessageRequest androidRequest)
    {
        Response response = null;
        AndroidSendMessageResponse androidResponse = null;
        ResponseBuilder builder = null;
        switch (responseType) {
            case INVALID_DATA_KEY:
                androidResponse = getEmptyAndroidSendMessageResponse();
                androidResponse.results[0].error = GCMErrorMessage.INVALID_DATA_KEY.getValue();
                builder = Response.ok(androidResponse, MediaType.APPLICATION_JSON_TYPE);
                break;
            case UNREGISTERED_DEVICE:
                androidResponse = getEmptyAndroidSendMessageResponse();
                androidResponse.results[0].error = GCMErrorMessage.UNREGISTERED_DEVICE.getValue();
                builder = Response.ok(androidResponse, MediaType.APPLICATION_JSON_TYPE);
                break;
            case MISSING_REGISTRATION_ID:
                androidResponse = getEmptyAndroidSendMessageResponse();
                androidResponse.results[0].error = GCMErrorMessage.MISSING_REGISTRATION_ID.getValue();
                builder = Response.ok(androidResponse, MediaType.APPLICATION_JSON_TYPE);
                break;
            case NEW_CANONICAL_ID:
                androidResponse = getEmptyAndroidSendMessageResponse();
                androidResponse.canonicalRegistrationIdCount = 1;
                androidResponse.failedMessageCount = 0;
                androidResponse.successfulMessageCount = 1;
                androidResponse.results[0].messageId = RandomStringUtils.random(8);
                androidResponse.results[0].registrationId = RandomStringUtils.random(64);
                builder = Response.ok(androidResponse, MediaType.APPLICATION_JSON_TYPE);
                break;
            case INTERNAL_SERVER_ERROR_FROM_RESULT:
                androidResponse = getEmptyAndroidSendMessageResponse();
                androidResponse.results[0].error = GCMErrorMessage.INTERNAL_SERVER_ERROR.getValue();
                builder = Response.ok(androidResponse, MediaType.APPLICATION_JSON_TYPE);
                break;
            case SERVER_TIMEOUT_FROM_RESULT:
                androidResponse = getEmptyAndroidSendMessageResponse();
                androidResponse.results[0].error = GCMErrorMessage.UNAVAILABLE.getValue();
                builder = Response.ok(androidResponse, MediaType.APPLICATION_JSON_TYPE);
                break;
            case INTERNAL_SERVER_ERROR_FROM_STATUS:
                builder = Response.serverError();
                break;
            case SERVER_TIMEOUT_FROM_STATUS:
                builder = Response.status(550);
                break;
            case UNKNOWN_ERROR:
                // For testing purposes, set the response status on the AndroidUnknownException to no-content.
                builder = Response.noContent();
                break;
        }
        response = builder.build();
        if (androidResponse != null) {
            androidResponse.response = response;
        }
        return response;
    }

    public MockAndroidConnection(ResponseType responseType)
    {
        this.responseType = responseType;
    }
}

