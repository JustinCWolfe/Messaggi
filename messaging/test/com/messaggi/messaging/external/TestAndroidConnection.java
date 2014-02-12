package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatformAndroidTesting;
import com.messaggi.TestDataHelper.Device1;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.Device3;
import com.messaggi.TestDataHelper.Device4;
import com.messaggi.TestDataHelper.Device5;
import com.messaggi.TestDataHelper.Device6;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.external.MessagingServiceConnectionFactory;
import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.AndroidConnection.AndroidSendMessageException;
import com.messaggi.messaging.external.AndroidConnection.AndroidSendMessageResponse;

public class TestAndroidConnection extends MessaggiTestCase
{
    private static final String MESSAGE1_TEXT = "Some message text";

    private static final String MESSAGE2_TEXT = "Some other message text";

    private static final ApplicationPlatform APP_PLAT = ApplicationPlatformAndroidTesting.getDomainObject();

    private static MessagingServiceConnection androidConnection;

    private static final Device D1 = Device1.getDomainObject();

    private static final Device D2 = Device2.getDomainObject();

    private static final Device D3 = Device3.getDomainObject();

    private static final Device D4 = Device4.getDomainObject();

    private static final Device D5 = Device5.getDomainObject();

    private static final Device D6 = Device6.getDomainObject();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        androidConnection = MessagingServiceConnectionFactory.Instance.getInstance().create(APP_PLAT);
        assertSame(APP_PLAT, androidConnection.getApplicationPlatform());
    }

    @Test
    public void testConnect() throws Exception
    {
        // The HTTP android connection has no connect implementation.
        androidConnection.connect();
    }

    @Test
    public void testSendMessage_InvalidAPIKey() throws Exception
    {
        Device[] to = { D2 };
        ApplicationPlatform invalidAPIKeyAppPlat = ApplicationPlatform1.getDomainObject();
        MessagingServiceConnection connection = MessagingServiceConnectionFactory.Instance.getInstance().create(
                invalidAPIKeyAppPlat);
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidSendMessageException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.UNAUTHORIZED.getFamily(), androidResponse.response.getStatusInfo().getFamily());
            assertEquals(Response.Status.UNAUTHORIZED.getReasonPhrase(), androidResponse.response.getStatusInfo()
                    .getReasonPhrase());
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), androidResponse.response.getStatusInfo()
                    .getStatusCode());
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_InvalidRegistrationIDKey_Single() throws Exception
    {
        Device[] to = { D2 };
        // Don't run in debug mode because in debug mode the registration ids aren't checked.
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        try {
            androidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidSendMessageException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.OK.getFamily(), androidResponse.response.getStatusInfo().getFamily());
            assertEquals(Response.Status.OK.getReasonPhrase(), androidResponse.response.getStatusInfo()
                    .getReasonPhrase());
            assertEquals(Response.Status.OK.getStatusCode(), androidResponse.response.getStatusInfo().getStatusCode());
            assertThat(1L, equalTo(androidResponse.failedMessageCount));
            assertThat(0L, equalTo(androidResponse.canonicalRegistrationIdCount));
            assertThat(0L, greaterThan(androidResponse.multicastId));
            assertThat(0L, equalTo(androidResponse.successfulMessageCount));
            assertThat("InvalidRegistration", equalTo(androidResponse.results[0].error));
            assertThat(androidResponse.results[0].messageId, nullValue());
            assertThat(0L, equalTo(androidResponse.results[0].registrationId));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_InvalidRegistrationIDKey_Multiple() throws Exception
    {
        Device[] to = { D2, D3, D4, D5, D6 };
        // Don't run in debug mode because in debug mode the registration ids aren't checked.
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        try {
            androidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidSendMessageException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.OK.getFamily(), androidResponse.response.getStatusInfo().getFamily());
            assertEquals(Response.Status.OK.getReasonPhrase(), androidResponse.response.getStatusInfo()
                    .getReasonPhrase());
            assertEquals(Response.Status.OK.getStatusCode(), androidResponse.response.getStatusInfo().getStatusCode());
            assertThat(5L, equalTo(androidResponse.failedMessageCount));
            assertThat(0L, equalTo(androidResponse.canonicalRegistrationIdCount));
            assertThat(-1L, equalTo(androidResponse.multicastId));
            assertThat(0L, equalTo(androidResponse.successfulMessageCount));
            for (int toIndex = 0; toIndex < to.length; toIndex++) {
                assertThat("InvalidRegistration", equalTo(androidResponse.results[toIndex].error));
                assertThat(androidResponse.results[toIndex].messageId, nullValue());
                assertThat(0L, equalTo(androidResponse.results[toIndex].registrationId));
            }
            return;
        }
        fail("should not get here");
    }

    @Test
    @Ignore
    public void testSendMessage_ValidRegistrationID_Single() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        SendMessageResponse response = androidConnection.sendMessage(request);
        assertTrue(response instanceof AndroidSendMessageResponse);
        AndroidSendMessageResponse androidResponse = (AndroidSendMessageResponse) response;
        assertThat(0L, equalTo(androidResponse.failedMessageCount));
        assertThat(0L, equalTo(androidResponse.canonicalRegistrationIdCount));
        assertThat(0L, greaterThan(androidResponse.multicastId));
        assertThat(1L, equalTo(androidResponse.successfulMessageCount));
    }

    @Test
    @Ignore
    public void testSendMessage_ValidRegistrationID_Multiple() throws Exception
    {

    }

    @Test
    @Ignore
    public void testSendMessage_ValidRegistrationIDWithDifferentCanonicalId_Single() throws Exception
    {
    }

    @Test
    @Ignore
    public void testSendMessage_ValidRegistrationIDWithDifferentCanonicalId_Multiple() throws Exception
    {
    }

    @Test
    @Ignore
    public void testSendMessage_MixOfValidAndInvalidRegistrationIDs() throws Exception
    {

    }
}

