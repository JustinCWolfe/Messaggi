package com.messaggi.messaging.external;

import javax.ws.rs.core.Response;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleMulticastException;

public class AppleConnection implements MessagingServiceConnection
{
    // APNs production host - this is set to a different host for unit testing (via a mock object).
    protected static String APPLE_PUSH_NOTIFICATION_HOST = "gateway.push.apple.com";

    private static final int APPLE_PUSH_NOTIFICATION_PORT = 2195;

    //TODO: start unit testing.
    //TODO: add error handling.
    //TODO: add support for the feedback service.

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

    private void getAPNsServerCertificate()
    {

    }

    private void validateAPNsServerCertificate()
    {

    }

    private void sendProviderCertificatetoAPNs()
    {

    }

    /**
     * Note: To establish a TLS session with APNs, an Entrust Secure CA root
     * certificate must be installed on the provider’s server. If the server is
     * running OS X, this root certificate is already in the keychain. On other
     * systems, the certificate might not be available. You can download this
     * certificate from the Entrust SSL Certificates website.
     */
    @Override
    public void connect() throws Exception
    {
        // When a provider authenticates itself to APNs, it sends its topic to the APNs server, which identifies the 
        // application for which it’s providing data. The topic is currently the bundle identifier of the target application.

        // Connection trust between a provider and APNs is also established through TLS peer-to-peer authentication. 
        // The provider initiates a TLS connection, gets the server certificate from APNs, and validates that certificate. 
        // Then the provider sends its provider certificate to APNs, which validates it on its end. Once this procedure 
        // is complete, a secure TLS connection has been established; APNs is now satisfied that the connection has been 
        // made by a legitimate provider. 
        getAPNsServerCertificate();
        validateAPNsServerCertificate();
        sendProviderCertificatetoAPNs();
    }

    protected Response sendMessageInternal(AppleSendMessageRequest appleRequest)
    {
        // Note that for android HTTP, the application platform  is not required since we do not 
        // establish a stateful connection with the messaging service.
        //Invocation.Builder invocationBuilder = SEND_MESSAGE_WEB_TARGET.request(MediaType.APPLICATION_JSON_TYPE);
        //String authentication = String.format(AUTHORIZATION_HEADER_VALUE_FORMAT,
        //        applicationPlatform.getExternalServiceToken());
        //invocationBuilder.header(AUTHORIZATION_HEADER_NAME, authentication);
        //Entity<AndroidSendMessageRequest> entity = Entity.entity(androidRequest, MediaType.APPLICATION_JSON_TYPE);
        //return invocationBuilder.post(entity);
        return null;
    }

    protected AndroidSendMessageResponse getEntityFromResponse(Response response)
    {
        return response.readEntity(AndroidSendMessageResponse.class);
    }

    protected SendMessageResponse processSendMessageResponse(AppleSendMessageRequest appleRequest, Response response)
        throws SendMessageException
    {
        // Create generic apple response based on the jaxb response.
        AndroidSendMessageResponse androidResponse = new AndroidSendMessageResponse(response);
        if (Response.Status.OK.getStatusCode() == response.getStatusInfo().getStatusCode()) {
            androidResponse = getEntityFromResponse(response);
            androidResponse.response = response;
            return androidResponse;
        }
        return androidResponse;
        //throw AndroidExceptionFactory.createSendMessageException(appleRequest, androidResponse);
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        AppleSendMessageRequest appleRequest = new AppleSendMessageRequest(request);
        if (appleRequest.request.to.length > 1) {
            throw new AppleMulticastException(appleRequest, null);
        }
        Response response = sendMessageInternal(appleRequest);
        return processSendMessageResponse(appleRequest, response);
    }
}

