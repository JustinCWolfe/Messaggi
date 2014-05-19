package com.messaggi.messaging.external.connection;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.TestDataHelper.ApplicationPlatformAppleTesting;
import com.messaggi.TestDataHelper.DeviceAppleTesting1;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.connection.MessagingServiceConnections;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.SendMessageResponse;
import com.messaggi.messaging.external.message.AppleSendMessageResponse;
import com.messaggi.messaging.external.message.exception.AppleSendMessageException.AppleInvalidPayloadException;
import com.messaggi.messaging.external.message.exception.AppleSendMessageException.AppleMulticastException;
import com.messaggi.messaging.external.message.exception.AppleSendMessageException.AppleNotConnectedException;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.internal.QueuedApnsService;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.NetworkIOException;

public class TestAppleConnection extends ConnectionTestCase
{
    private static final Device VALID_D1 = DeviceAppleTesting1.getDomainObject();

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
    @After
    public void tearDown() throws Exception
    {
        connection.disconnect();
        super.tearDown();
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
    public void testSendMessage_AppleInvalidPayloadException() throws Exception
    {
        Device[] to = { D2 };
        connectAndValidate(connection);
        Map<String, String> invalidMessage = new HashMap<>(MESSAGE_MAP);
        // Put a second message into the map which should cause an invalid message exception.
        invalidMessage.put("key2", "Second message text");
        SendMessageRequest request = new SendMessageRequest(D1, to, invalidMessage);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AppleInvalidPayloadException e) {
            assertThat(e.request.request, sameInstance(request));
            assertThat(e.response, nullValue());
            return;
        }
        fail("should not get here");
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
    public void testSendMessage_AppleNotConnectedException() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AppleNotConnectedException e) {
            assertThat(e.request.request, sameInstance(request));
            assertThat(e.response, nullValue());
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage() throws Exception
    {
        Device[] to = { D2 };
        connectAndValidate(connection);
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE1_VALUE);
        SendMessageResponse response = connection.sendMessage(request);
        assertThat(response, notNullValue());
        assertThat(response, instanceOf(AppleSendMessageResponse.class));
    }

    @Test
    public void testSendMessage_Multiple() throws Exception
    {
        Device[] to = { VALID_D1 };
        connectAndValidate(connection);
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE1_VALUE);
        SendMessageResponse response = connection.sendMessage(request);
        assertThat(response, notNullValue());
        assertThat(response, instanceOf(AppleSendMessageResponse.class));

        SendMessageResponse response2 = connection.sendMessage(request);
        assertThat(response2, notNullValue());
        assertThat(response2, instanceOf(AppleSendMessageResponse.class));

        SendMessageResponse response3 = connection.sendMessage(request);
        assertThat(response3, notNullValue());
        assertThat(response3, instanceOf(AppleSendMessageResponse.class));
    }
}

