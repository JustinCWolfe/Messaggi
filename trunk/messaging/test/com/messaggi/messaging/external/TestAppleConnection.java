package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

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
import com.messaggi.external.MessagingServiceConnections;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleInvalidConnectionException;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleMulticastException;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.internal.QueuedApnsService;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.NetworkIOException;

public class TestAppleConnection extends ConnectionTestCase
{
    private static final String MESSAGE1_KEY = "key1";

    private static final String MESSAGE1_VALUE = "First message text";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        connectionSuiteSetUp();
        MESSAGE_MAP.put(MESSAGE1_KEY, MESSAGE1_VALUE);
        APP_PLAT = ApplicationPlatformAppleTesting.getDomainObject();
        connection = new MockAppleConnection();
        connection.setApplicationPlatform(APP_PLAT);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        connectionSuiteTearDown();
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
        connection.disconnect();
    }

    private ApnsService getServiceReference(MessagingServiceConnection connection) throws Exception
    {
        Class<?> type = connection.getClass();
        Field serviceField = type.getSuperclass().getDeclaredField("service");
        serviceField.setAccessible(true);
        return (ApnsService) serviceField.get(connection);
    }
    
    private void connectAndValidate(MessagingServiceConnection connection) throws Exception
    {
        connection.connect();
        ApnsService service = getServiceReference(connection);
        assertThat(service, notNullValue());
        assertThat(service, instanceOf(QueuedApnsService.class));
    }

    @Test
    public void testMessagingServiceConnectionFactory() throws Exception
    {
        MessagingServiceConnection conn = MessagingServiceConnections.create(APP_PLAT);
        assertThat(conn.getApplicationPlatform(), sameInstance(APP_PLAT));
    }

    @Test
    public void testConnectWithNullExternalServiceToken() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        invalidAppPlat.setExternalServiceToken(null);
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        try {
            mockConnection.connect();
            fail("Should not get here");
        } catch (NetworkIOException e) {
            assertThat(
                    e.getMessage(),
                    containsString("javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure"));
            ApnsService service = getServiceReference(mockConnection);
            assertThat(service, notNullValue());
            assertThat(service, instanceOf(QueuedApnsService.class));
            return;
        }
        fail("Should not get here");
    }

    @Test
    public void testConnectWithInvalidExternalServiceToken_Reversed() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        byte[] externalServiceTokenBytes = invalidAppPlat.getExternalServiceTokenAsBinary();
        ArrayUtils.reverse(externalServiceTokenBytes);
        invalidAppPlat.setExternalServiceTokenAsBinary(externalServiceTokenBytes);
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        try {
            mockConnection.connect();
            fail("Should not get here");
        } catch (InvalidSSLConfig e) {
            assertThat(e.getMessage(), containsString("java.io.IOException: toDerInputStream rejects tag type 1"));
            assertThat(getServiceReference(mockConnection), nullValue());
            return;
        }
        fail("Should not get here");
    }

    @Test
    public void testConnectWithInvalidExternalServiceToken_TooShort() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        invalidAppPlat.setExternalServiceToken("something invalid");
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        try {
            mockConnection.connect();
            fail("Should not get here");
        } catch (InvalidSSLConfig e) {
            assertThat(e.getMessage(), containsString("java.io.EOFException: Detect premature EOF"));
            assertThat(getServiceReference(mockConnection), nullValue());
            return;
        }
        fail("Should not get here");
    }

    @Test
    public void testConnectWithNullExternalServicePassword() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        invalidAppPlat.setExternalServicePassword(null);
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        try {
            mockConnection.connect();
            fail("Should not get here");
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    containsString("Passwords must be specified.Oracle Java SDK does not support passwordless p12 certificates"));
            assertThat(getServiceReference(mockConnection), nullValue());
            return;
        }
        fail("Should not get here");
    }

    @Test
    public void testConnectWithInvalidExternalServicePassword() throws Exception
    {
        ApplicationPlatform invalidAppPlat = ApplicationPlatformAppleTesting.getDomainObject();
        invalidAppPlat.setExternalServicePassword("something invalid");
        MockAppleConnection mockConnection = new MockAppleConnection();
        mockConnection.setApplicationPlatform(invalidAppPlat);
        try {
            mockConnection.connect();
            fail("Should not get here");
        } catch (InvalidSSLConfig e) {
            assertThat(e.getMessage(), containsString("java.io.IOException: Sequence tag error"));
            assertThat(getServiceReference(mockConnection), nullValue());
            return;
        }
        fail("Should not get here");
    }

    @Test
    public void testConnect() throws Exception
    {
        connectAndValidate(connection);
        ApnsService service = getServiceReference(connection);
        service.testConnection();
        service.stop();
    }

    @Test
    public void testDisconnect() throws Exception
    {
        assertThat(getServiceReference(connection), nullValue());
        connection.disconnect();
        assertThat(getServiceReference(connection), nullValue());
        connectAndValidate(connection);
        assertThat(getServiceReference(connection), notNullValue());
        connection.disconnect();
        assertThat(getServiceReference(connection), nullValue());
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
    public void testSendMessage_AppleInvalidConnectionException() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AppleInvalidConnectionException e) {
            assertThat(e.request.request, sameInstance(request));
            assertThat(e.response, nullValue());
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_NoToDevice() throws Exception
    {
        connectAndValidate(connection);
        SendMessageRequest request = new SendMessageRequest(D1, null, MESSAGE_MAP);
        SendMessageResponse response = connection.sendMessage(request);
    }

    @Test
    public void testSendMessage() throws Exception
    {
        Device[] to = { D2 };
        connectAndValidate(connection);
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        SendMessageResponse response = connection.sendMessage(request);
        assertTrue(response instanceof AppleSendMessageResponse);
        AppleSendMessageResponse appleResponse = (AppleSendMessageResponse) response;
    }
}

