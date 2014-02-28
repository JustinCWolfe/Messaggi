package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.messaggi.TestDataHelper.ApplicationPlatformAppleTesting;
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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MESSAGE_MAP.put(MESSAGE1_KEY, MESSAGE1_VALUE);
        APP_PLAT = ApplicationPlatformAppleTesting.getDomainObject();
        connection = new MockAppleConnection();
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

    @Test
    public void testMessagingServiceConnectionFactory() throws Exception
    {
        MessagingServiceConnection conn = MessagingServiceConnectionFactory.Instance.getInstance().create(APP_PLAT);
        assertThat(APP_PLAT, sameInstance(conn.getApplicationPlatform()));
    }

    @Test
    @Ignore
    public void testConnect() throws Exception
    {
        connection.connect();
    }

    @Test
    @Ignore
    public void testDisconnect() throws Exception
    {
        connection.disconnect();
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
        //assertThat(0L, equalTo(appleResponse.failedMessageCount));
        //assertThat(0L, equalTo(appleResponse.canonicalRegistrationIdCount));
        //assertThat(appleResponse.multicastId, greaterThan(0L));
        //assertThat(1L, equalTo(appleResponse.successfulMessageCount));
        //assertThat(appleResponse.results[0].error, nullValue());
        //assertThat(appleResponse.results[0].messageId, notNullValue());
        //assertThat(appleResponse.results[0].registrationId, nullValue());
    }
}

