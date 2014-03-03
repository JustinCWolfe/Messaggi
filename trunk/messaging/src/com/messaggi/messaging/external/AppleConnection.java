package com.messaggi.messaging.external;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.Response;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleMulticastException;

public class AppleConnection implements MessagingServiceConnection
{
    private static final String KEYSTORE_TYPE = "PKCS12";

    private static final String KEY_ALGORITHM = "sunx509";

    private static String KEYSTORE_PASSWORD = "jwolfema2226";

    // APNs production host - this is set to a different host for unit testing (via a mock object).
    protected static String APPLE_PUSH_NOTIFICATION_HOST = "gateway.push.apple.com";

    private static final int APPLE_PUSH_NOTIFICATION_PORT = 2195;

    private SSLSocket apnsSSLSocket;

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
        // Connection trust between a provider and APNs is also established through TLS peer-to-peer authentication. 
        // The provider initiates a TLS connection, gets the server certificate from APNs, and validates that certificate. 
        // Then the provider sends its provider certificate to APNs, which validates it on its end. Once this procedure 
        // is complete, a secure TLS connection has been established; APNs is now satisfied that the connection has been 
        // made by a legitimate provider. 
        // When a provider authenticates itself to APNs, it sends its topic to the APNs server, which identifies the 
        // application for which it’s providing data. The topic is currently the bundle identifier of the target application.
        char[] passwordKey = KEYSTORE_PASSWORD.toCharArray();

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        byte[] authentication = applicationPlatform.getExternalServiceTokenAsBinary();
        ByteArrayInputStream authenticationStream = new ByteArrayInputStream(authentication);
        keyStore.load(authenticationStream, passwordKey);

        // Get a KeyManager and initialize it
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_ALGORITHM);
        keyManagerFactory.init(keyStore, passwordKey);

        // Get a TrustManagerFactory with the DEFAULT KEYSTORE, so we have all
        // the certificates in cacerts trusted
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KEY_ALGORITHM);
        trustManagerFactory.init((KeyStore) null);

        // Get the SSLContext to help create SSLSocketFactory
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        SSLSocketFactory factory = sslContext.getSocketFactory();
        apnsSSLSocket = (SSLSocket) factory.createSocket(APPLE_PUSH_NOTIFICATION_HOST, APPLE_PUSH_NOTIFICATION_PORT);
    }

    @Override
    public void disconnect() throws Exception
    {
        if (apnsSSLSocket != null) {
            apnsSSLSocket.close();
        }
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

