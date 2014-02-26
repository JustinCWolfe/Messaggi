package com.messaggi.messaging.external.exception;

import javax.ws.rs.core.Response;

import com.messaggi.messaging.external.AndroidSendMessageRequest;
import com.messaggi.messaging.external.AndroidSendMessageResponse;
import com.messaggi.messaging.external.AndroidSendMessageResponse.AndroidResult;
import com.messaggi.messaging.external.AndroidSendMessageResponse.AndroidResult.GCMErrorMessage;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidAuthenticationErrorException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidCanonicalIdException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidInternalServerErrorException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidInvalidDataKeyException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidInvalidRegistrationIdException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidMessageTooBigException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidMissingRegistrationIdException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidTimeoutException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidUnknownException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidUnregisteredDeviceException;

public class AndroidExceptionFactory
{
    public static AndroidSendMessageException createSendMessageException(AndroidSendMessageRequest androidRequest,
            AndroidSendMessageResponse androidResponse)
    {
        int responseStatusCode = androidResponse.response.getStatusInfo().getStatusCode();
        boolean isOk = (responseStatusCode == Response.Status.OK.getStatusCode());
        boolean isServerError = (responseStatusCode == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        boolean isTimeout = (responseStatusCode > 500 && responseStatusCode < 600);
        boolean isUnauthorized = (responseStatusCode == Response.Status.UNAUTHORIZED.getStatusCode());
        boolean isBadRequest = (responseStatusCode == Response.Status.BAD_REQUEST.getStatusCode());
        if (isBadRequest) {
            return new AndroidMissingRegistrationIdException(androidRequest, androidResponse);
        } else if (isUnauthorized) {
            return new AndroidAuthenticationErrorException(androidRequest, androidResponse);
        } else if (isServerError || responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.INTERNAL_SERVER_ERROR)) {
            return new AndroidInternalServerErrorException(androidRequest, androidResponse);
        } else if (isTimeout || responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.UNAVAILABLE)) {
            return new AndroidTimeoutException(androidRequest, androidResponse);
        } else if (isOk) {
            if (responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.INVALID_DATA_KEY)) {
                return new AndroidInvalidDataKeyException(androidRequest, androidResponse);
            } else if (responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.MESSAGE_TOO_BIG)) {
                return new AndroidMessageTooBigException(androidRequest, androidResponse);
            } else if (responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.INVALID_REGISTRATION_ID)) {
                return new AndroidInvalidRegistrationIdException(androidRequest, androidResponse);
            } else if (responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.UNREGISTERED_DEVICE)) {
                return new AndroidUnregisteredDeviceException(androidRequest, androidResponse);
            } else if (responseHasGCMErrorMessage(androidResponse, GCMErrorMessage.MISSING_REGISTRATION_ID)) {
                return new AndroidMissingRegistrationIdException(androidRequest, androidResponse);
            } else if (androidResponse.canonicalRegistrationIdCount > 0) {
                return new AndroidCanonicalIdException(androidRequest, androidResponse);
            }
        }
        return new AndroidUnknownException (androidRequest, androidResponse);
    }

    private static boolean responseHasGCMErrorMessage(AndroidSendMessageResponse androidResponse,
            GCMErrorMessage errorMessageToSearchFor)
    {
        if (androidResponse.results != null) {
            for (AndroidResult androidResult : androidResponse.results) {
                if (androidResult.getGCMErrorMessage() == errorMessageToSearchFor) {
                    return true;
                }
            }
        }
        return false;
    }
}

