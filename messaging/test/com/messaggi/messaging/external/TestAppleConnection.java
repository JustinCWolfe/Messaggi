package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import javax.net.ssl.SSLSocket;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.TestDataHelper.ApplicationPlatformAppleTesting;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.external.MessagingServiceConnectionFactory;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleMulticastException;

public class TestAppleConnection extends ConnectionTestCase
{
    private static final String MESSAGE1_KEY = "key1";

    private static final String MESSAGE1_VALUE = "First message text";

    private SSLSocket apnsSSLSocket;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MESSAGE_MAP.put(MESSAGE1_KEY, MESSAGE1_VALUE);
        APP_PLAT = ApplicationPlatformAppleTesting.getDomainObject();
        connection = new MockAppleConnection();
        connection.setApplicationPlatform(APP_PLAT);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {

    }

    @Override
    @Before
    public void setUp() throws Exception
    {
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
    }

    private void createSocketReference() throws Exception
    {
        Class<?> type = connection.getClass();
        Field socketField = type.getSuperclass().getDeclaredField("apnsSSLSocket");
        socketField.setAccessible(true);
        apnsSSLSocket = (SSLSocket) socketField.get(connection);
    }

    @Test
    public void testMessagingServiceConnectionFactory() throws Exception
    {
        MessagingServiceConnection conn = MessagingServiceConnectionFactory.Instance.getInstance().create(APP_PLAT);
        assertThat(conn.getApplicationPlatform(), sameInstance(APP_PLAT));
    }
    
    private void connectAndValidate() throws Exception
    {
        connection.connect();
        createSocketReference();
        assertThat(apnsSSLSocket, notNullValue());
        assertTrue(apnsSSLSocket.isConnected());
        assertFalse(apnsSSLSocket.isClosed());
        assertFalse(apnsSSLSocket.isInputShutdown());
        assertFalse(apnsSSLSocket.isOutputShutdown());
    }

    @Test
    public void testConnectWithNullExternalServiceToken() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        invalidAppPlat.setExternalServiceToken(null);
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        connection.connect();
        createSocketReference();
        assertThat(apnsSSLSocket, nullValue());
    }

    @Test
    public void testConnectWithInvalidExternalServiceToken() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        byte[] externalServiceTokenBytes = invalidAppPlat.getExternalServiceTokenAsBinary();
        ArrayUtils.reverse(externalServiceTokenBytes);
        invalidAppPlat.setExternalServiceTokenAsBinary(externalServiceTokenBytes);
        invalidAppPlat.setExternalServiceToken("something invalid");
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        connection.connect();
        createSocketReference();
        assertThat(apnsSSLSocket, nullValue());
    }

    @Test
    public void testConnectWithNullExternalServicePassword() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        invalidAppPlat.setExternalServicePassword(null);
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        connection.connect();
        createSocketReference();
        assertThat(apnsSSLSocket, nullValue());
    }

    @Test
    public void testConnectWithInvalidExternalServicePassword() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        byte[] externalServicePasswordBytes = invalidAppPlat.getExternalServicePasswordAsBinary();
        ArrayUtils.reverse(externalServicePasswordBytes);
        invalidAppPlat.setExternalServicePasswordAsBinary(externalServicePasswordBytes);
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        connection.connect();
        createSocketReference();
        assertThat(apnsSSLSocket, nullValue());
    }

    @Test
    public void testConnect() throws Exception
    {
        connectAndValidate();
        apnsSSLSocket.close();
    }

    @Test
    public void testDisconnect() throws Exception
    {
        createSocketReference();
        assertThat(apnsSSLSocket, nullValue());
        connection.disconnect();
        assertThat(apnsSSLSocket, nullValue());
        connectAndValidate();
        connection.disconnect();
        assertThat(apnsSSLSocket, notNullValue());
        assertTrue(apnsSSLSocket.isClosed());
    }

    @Test
    public void testSendMessage_AppleMulticastException() throws Exception
    {
        Device[] to = { D2, D3, D4, D5, D6 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AppleMulticastException e) {
            assertThat(e.response, nullValue());
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        SendMessageResponse response = connection.sendMessage(request);
        assertTrue(response instanceof AppleSendMessageResponse);
        AppleSendMessageResponse appleResponse = (AppleSendMessageResponse) response;
        //assertThat(appleResponse.failedMessageCount), equalTo(0L);
        //assertThat(appleResponse.canonicalRegistrationIdCount, equalTo(0L));
        //assertThat(appleResponse.multicastId, greaterThan(0L));
        //assertThat(appleResponse.successfulMessageCount, equalTo(1L));
        //assertThat(appleResponse.results[0].error, nullValue());
        //assertThat(appleResponse.results[0].messageId, notNullValue());
        //assertThat(appleResponse.results[0].registrationId, nullValue());
    }
}

