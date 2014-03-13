package com.messaggi.messaging.external;

import java.io.ByteArrayInputStream;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleMulticastException;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleNotConnectedException;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.EnhancedApnsNotification;

public class AppleConnection implements MessagingServiceConnection
{
    private ApnsService service;

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

    protected void setBuilderDestination(ApnsServiceBuilder builder)
    {
        builder.withProductionDestination();
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
        byte[] authentication = applicationPlatform.getExternalServiceTokenAsBinary();
        ByteArrayInputStream authenticationStream = (authentication != null) ? new ByteArrayInputStream(authentication)
                : null;
        ApnsServiceBuilder serviceBuilder = APNS.newService()
                .withCert(authenticationStream, applicationPlatform.getExternalServicePassword()).asQueued();
        setBuilderDestination(serviceBuilder);
        service = serviceBuilder.build();
        service.testConnection();
    }

    @Override
    public void disconnect() throws Exception
    {
        if (service != null) {
            service.stop();
            service = null;
        }
    }

    public void testConnection() throws Exception
    {
        if (service != null) {
            service.testConnection();
        }
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        AppleSendMessageRequest appleRequest = new AppleSendMessageRequest(request);
        if (appleRequest.request.to.length > 1) {
            throw new AppleMulticastException(appleRequest, null);
        }
        if (service == null) {
            throw new AppleNotConnectedException(appleRequest, null);
        }
        EnhancedApnsNotification notification = new EnhancedApnsNotification(appleRequest.notificationId,
                appleRequest.expirationDate, appleRequest.deviceToken, appleRequest.payloadBytes);
        service.push(notification);
        return new AppleSendMessageResponse();
    }
}

