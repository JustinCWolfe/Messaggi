package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Strings;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatformAndroidTesting;
import com.messaggi.TestDataHelper.Device1;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.Device3;
import com.messaggi.TestDataHelper.Device4;
import com.messaggi.TestDataHelper.Device5;
import com.messaggi.TestDataHelper.Device6;
import com.messaggi.TestDataHelper.DeviceAndroidTesting1;
import com.messaggi.TestDataHelper.DeviceAndroidTesting2;
import com.messaggi.TestDataHelper.DeviceAndroidTesting3;
import com.messaggi.TestDataHelper.DeviceAndroidTesting4;
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
    private static final String MESSAGE1_KEY = "key1";

    private static final String MESSAGE1_VALUE = "First message text";

    private static final String MESSAGE2_KEY = "key2";

    private static final String MESSAGE2_VALUE = "Second message text";

    private static final String MESSAGE3_KEY_INVALID_WORD = "google.com";

    private static final String MESSAGE3_VALUE_INVALID_WORD = "Third message text";

    private static final String MESSAGE4_KEY_TOO_BIG = "too_big";

    private static final String MESSAGE4_VALUE_TOO_BIG = Strings.repeat("0123456789", 500);

    private static final Map<String, String> MESSAGE_MAP = new HashMap<>();

    private static final ApplicationPlatform APP_PLAT = ApplicationPlatformAndroidTesting.getDomainObject();

    private static MessagingServiceConnection androidConnection;

    private static final Device D1 = Device1.getDomainObject();

    private static final Device D2 = Device2.getDomainObject();

    private static final Device D3 = Device3.getDomainObject();

    private static final Device D4 = Device4.getDomainObject();

    private static final Device D5 = Device5.getDomainObject();

    private static final Device D6 = Device6.getDomainObject();

    private static final Device VALID_D1 = DeviceAndroidTesting1.getDomainObject();

    private static final Device VALID_D2 = DeviceAndroidTesting2.getDomainObject();

    private static final Device VALID_D3 = DeviceAndroidTesting3.getDomainObject();

    private static final Device UNREGISTERED_D4 = DeviceAndroidTesting4.getDomainObject();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MESSAGE_MAP.put(MESSAGE1_KEY, MESSAGE1_VALUE);
        MESSAGE_MAP.put(MESSAGE2_KEY, MESSAGE2_VALUE);
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
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
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
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
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
            assertThat(-1L, equalTo(androidResponse.multicastId));
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
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
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
    public void testSendMessage_ValidRegistrationID_Single() throws Exception
    {
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
        SendMessageResponse response = androidConnection.sendMessage(request);
        assertTrue(response instanceof AndroidSendMessageResponse);
        AndroidSendMessageResponse androidResponse = (AndroidSendMessageResponse) response;
        assertThat(0L, equalTo(androidResponse.failedMessageCount));
        assertThat(0L, equalTo(androidResponse.canonicalRegistrationIdCount));
        assertThat(-1L, equalTo(androidResponse.multicastId));
        assertThat(1L, equalTo(androidResponse.successfulMessageCount));
    }

    @Test
    public void testSendMessage_UnregisteredID() throws Exception
    {
        Device[] to = { UNREGISTERED_D4 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
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
            assertThat(-1L, equalTo(androidResponse.multicastId));
            assertThat(0L, equalTo(androidResponse.successfulMessageCount));
            assertThat("NotRegistered", equalTo(androidResponse.results[0].error));
            assertThat(androidResponse.results[0].messageId, nullValue());
            assertThat(0L, equalTo(androidResponse.results[0].registrationId));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_ValidRegistrationIDWithInvalidWordInMessageKey() throws Exception
    {
        Map<String, String> invalidMessageMap = new HashMap<>(MESSAGE_MAP);
        invalidMessageMap.put(MESSAGE3_KEY_INVALID_WORD, MESSAGE3_VALUE_INVALID_WORD);
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, invalidMessageMap, true);
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
            assertThat(-1L, equalTo(androidResponse.multicastId));
            assertThat(0L, equalTo(androidResponse.successfulMessageCount));
            assertThat("InvalidDataKey", equalTo(androidResponse.results[0].error));
            assertThat(androidResponse.results[0].messageId, nullValue());
            assertThat(0L, equalTo(androidResponse.results[0].registrationId));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_ValidRegistrationIDWithMessageThatIsTooBig() throws Exception
    {
        Map<String, String> invalidMessageMap = new HashMap<>(MESSAGE_MAP);
        invalidMessageMap.put(MESSAGE4_KEY_TOO_BIG, MESSAGE4_VALUE_TOO_BIG);
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, invalidMessageMap, true);
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
            assertThat(-1L, equalTo(androidResponse.multicastId));
            assertThat(0L, equalTo(androidResponse.successfulMessageCount));
            assertThat("MessageTooBig", equalTo(androidResponse.results[0].error));
            assertThat(androidResponse.results[0].messageId, nullValue());
            assertThat(0L, equalTo(androidResponse.results[0].registrationId));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_ValidRegistrationID_Multiple() throws Exception
    {
        Device[] to = { VALID_D1, VALID_D2, VALID_D3 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
        SendMessageResponse response = androidConnection.sendMessage(request);
        assertTrue(response instanceof AndroidSendMessageResponse);
        AndroidSendMessageResponse androidResponse = (AndroidSendMessageResponse) response;
        assertThat(0L, equalTo(androidResponse.failedMessageCount));
        assertThat(0L, equalTo(androidResponse.canonicalRegistrationIdCount));
        assertThat(-1L, equalTo(androidResponse.multicastId));
        assertThat(3L, equalTo(androidResponse.successfulMessageCount));
    }

    @Test
    public void testSendMessage_MixOfValidAndInvalidRegistrationIDs() throws Exception
    {
        Device[] to = { D2, D3, D4, D5, D6, VALID_D1, VALID_D2, VALID_D3 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP, true);
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
            assertThat(3L, equalTo(androidResponse.successfulMessageCount));
            for (int toIndex = 0; toIndex < to.length; toIndex++) {
                // The first 5 are invalid registrations, the next 3 are valid.
                if (toIndex < 5) {
                    assertThat("InvalidRegistration", equalTo(androidResponse.results[toIndex].error));
                    assertThat(androidResponse.results[toIndex].messageId, nullValue());
                    assertThat(0L, equalTo(androidResponse.results[toIndex].registrationId));
                } else {
                    assertThat(androidResponse.results[toIndex].error, nullValue());
                    assertThat("fake_message_id", equalTo(androidResponse.results[toIndex].messageId));
                    assertThat(0L, equalTo(androidResponse.results[toIndex].registrationId));
                }
            }
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_ValidRegistrationIDWithDifferentCanonicalId_Single() throws Exception
    {
        fail("figure out how to test the canonical stuff");
    }

    @Test
    public void testSendMessage_ValidRegistrationIDWithDifferentCanonicalId_Multiple() throws Exception
    {
        fail("figure out how to test the canonical stuff");
    }

    @Test
    public void testSendMessage_ExponentialBackOff() throws Exception
    {
        // Errors in the 500-599 range (such as 500 or 503) indicate that there was an internal error 
        // in the GCM server while trying to process the request, or that the server is temporarily 
        // unavailable (for example, because of timeouts). Sender must retry later, honoring any 
        // Retry-After header included in the response. Application servers must implement exponential back-off.
        fail("figure out how to test the exponential back-off stuff");
    }
}

