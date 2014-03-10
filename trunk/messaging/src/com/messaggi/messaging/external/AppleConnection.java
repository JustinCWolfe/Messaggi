package com.messaggi.messaging.external;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.Response;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleMulticastException;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.ReconnectPolicy.Provided;

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
        ApnsServiceBuilder serviceBuilder = APNS.newService();
        byte[] authentication = applicationPlatform.getExternalServiceTokenAsBinary();
        ByteArrayInputStream authenticationStream = new ByteArrayInputStream(authentication);
        serviceBuilder.withCert(authenticationStream, applicationPlatform.getExternalServicePassword());
        serviceBuilder.withReconnectPolicy(Provided.NEVER);
        serviceBuilder.asQueued();
        setBuilderDestination(serviceBuilder);
        service = serviceBuilder.build();
        service.testConnection();
    }

    @Override
    public void disconnect() throws Exception
    {
        if (service != null) {
            service.stop();
        }
    }

    public void testConnection() throws Exception
    {
        if (service != null) {
            service.testConnection();
        }
    }

    private void sendData()
    {
        //send HTTP get request
        //BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sslSock.getOutputStream(), "UTF8"));            
        //wr.write("GET /mail HTTP/1.1\r\nhost: mail.google.com\r\n\r\n");
        //wr.flush();

        // read response
        //BufferedReader rd = new BufferedReader(new InputStreamReader(sslSock.getInputStream()));           
        //String string = null;

        //while ((string = rd.readLine()) != null) {
        //System.out.println(string);
        //System.out.flush();
        //}

        //rd.close();
        //wr.close(); 
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

