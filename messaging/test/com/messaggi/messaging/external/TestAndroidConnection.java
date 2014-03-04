package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Strings;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatformAndroidTesting;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.DeviceAndroidTesting1;
import com.messaggi.TestDataHelper.DeviceAndroidTesting2;
import com.messaggi.TestDataHelper.DeviceAndroidTesting3;
import com.messaggi.TestDataHelper.DeviceAndroidTesting4;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.external.MessagingServiceConnectionFactory;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;
import com.messaggi.messaging.external.AndroidSendMessageResponse.AndroidResult.GCMErrorMessage;
import com.messaggi.messaging.external.MockAndroidConnection.ResponseType;
import com.messaggi.messaging.external.exception.AndroidSendMessageException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidAuthenticationErrorException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidCanonicalIdException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidInternalServerErrorException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidInvalidDataKeyException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidInvalidRegistrationIdException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidMessageTooBigException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidMissingRegistrationIdException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidMulticastException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidTimeoutException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidUnknownException;
import com.messaggi.messaging.external.exception.AndroidSendMessageException.AndroidUnregisteredDeviceException;

public class TestAndroidConnection extends ConnectionTestCase
{
    private static final String MESSAGE1_KEY = "key1";

    private static final String MESSAGE1_VALUE = "First message text";

    private static final String MESSAGE2_KEY = "key2";

    private static final String MESSAGE2_VALUE = "Second message text";

    private static final String MESSAGE3_KEY_INVALID_WORD = "google.";

    private static final String MESSAGE3_VALUE_INVALID_WORD = "Third message text";

    private static final String MESSAGE4_KEY_INVALID_WORD = "from";

    private static final String MESSAGE4_VALUE_INVALID_WORD = "Fourth message text";

    private static final String MESSAGE5_KEY_TOO_BIG = "too_big";

    private static final String MESSAGE5_VALUE_TOO_BIG = Strings.repeat("0123456789", 500);

    private static final Device VALID_D1 = DeviceAndroidTesting1.getDomainObject();

    private static final Device VALID_D2 = DeviceAndroidTesting2.getDomainObject();

    private static final Device VALID_D3 = DeviceAndroidTesting3.getDomainObject();

    private static final Device UNREGISTERED_D4 = DeviceAndroidTesting4.getDomainObject();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MESSAGE_MAP.put(MESSAGE1_KEY, MESSAGE1_VALUE);
        MESSAGE_MAP.put(MESSAGE2_KEY, MESSAGE2_VALUE);
        APP_PLAT = ApplicationPlatformAndroidTesting.getDomainObject();
        connection = MessagingServiceConnectionFactory.Instance.getInstance().create(APP_PLAT);
        assertSame(APP_PLAT, connection.getApplicationPlatform());
    }

    private void validateUninitializedAndroidResponse(AndroidSendMessageResponse androidResponse)
    {
        assertThat(androidResponse.failedMessageCount, equalTo(0L));
        assertThat(androidResponse.canonicalRegistrationIdCount, equalTo(0L));
        assertThat(androidResponse.multicastId, equalTo(0L));
        assertThat(androidResponse.successfulMessageCount, equalTo(0L));
        assertThat(androidResponse.results, nullValue());
    }

    private void validateOkAndroidResponse(AndroidSendMessageResponse androidResponse)
    {
        assertEquals(Response.Status.OK.getFamily(), androidResponse.response.getStatusInfo().getFamily());
        assertEquals(Response.Status.OK.getReasonPhrase(), androidResponse.response.getStatusInfo().getReasonPhrase());
        assertEquals(Response.Status.OK.getStatusCode(), androidResponse.response.getStatusInfo().getStatusCode());
    }

    private void validateOkWithFailedMessageAndroidResponse(AndroidSendMessageResponse androidResponse)
    {
        validateOkAndroidResponse(androidResponse);
        assertThat(androidResponse.failedMessageCount, equalTo(1L));
        assertThat(androidResponse.canonicalRegistrationIdCount, equalTo(0L));
        assertThat(androidResponse.multicastId, greaterThan(0L));
        assertThat(androidResponse.successfulMessageCount, equalTo(0L));
    }

    private void validateOkWithFailedMessageAndErrorAndroidResponse(AndroidSendMessageResponse androidResponse,
            GCMErrorMessage gcmErrorMessage)
    {
        validateOkWithFailedMessageAndroidResponse(androidResponse);
        assertThat(androidResponse.results[0].getGCMErrorMessage(), equalTo(gcmErrorMessage));
    }

    @Test
    public void testMessagingServiceConnectionFactory() throws Exception
    {
        MessagingServiceConnection conn = MessagingServiceConnectionFactory.Instance.getInstance().create(APP_PLAT);
        assertThat(conn.getApplicationPlatform(), sameInstance(APP_PLAT));
    }

    @Test
    public void testConnect() throws Exception
    {
        // The HTTP android connection has no connect implementation.
        connection.connect();
    }

    @Test
    public void testDisconnect() throws Exception
    {
        // The HTTP android connection has no disconnect implementation.
        connection.disconnect();
    }

    @Test
    public void testSendMessage_AndroidMulticastException() throws Exception
    {
        Device[] to = { D2, D3, D4, D5, D6 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidMulticastException e) {
            assertThat(e.response, nullValue());
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidUnknownException() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(ResponseType.UNKNOWN_ERROR);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidUnknownException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(MockAndroidConnection.UNKNOWN_ERROR_STATUS.getFamily(), androidResponse.response
                    .getStatusInfo().getFamily());
            assertEquals(MockAndroidConnection.UNKNOWN_ERROR_STATUS.getReasonPhrase(), androidResponse.response
                    .getStatusInfo().getReasonPhrase());
            assertEquals(MockAndroidConnection.UNKNOWN_ERROR_STATUS.getStatusCode(), androidResponse.response
                    .getStatusInfo().getStatusCode());
            validateUninitializedAndroidResponse(androidResponse);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidAuthenticationErrorException() throws Exception
    {
        Device[] to = { D2 };
        ApplicationPlatform invalidAPIKeyAppPlat = ApplicationPlatform1.getDomainObject();
        MessagingServiceConnection connection = MessagingServiceConnectionFactory.Instance.getInstance().create(
                invalidAPIKeyAppPlat);
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidAuthenticationErrorException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.UNAUTHORIZED.getFamily(), androidResponse.response.getStatusInfo().getFamily());
            assertEquals(Response.Status.UNAUTHORIZED.getReasonPhrase(), androidResponse.response.getStatusInfo()
                    .getReasonPhrase());
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), androidResponse.response.getStatusInfo()
                    .getStatusCode());
            validateUninitializedAndroidResponse(androidResponse);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidInternalServerErrorExceptionFromResult() throws Exception
    {
        Device[] to = { VALID_D1 };
        // Don't run in debug mode because in debug mode the registration ids aren't checked.
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(
                    ResponseType.INTERNAL_SERVER_ERROR_FROM_RESULT);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidInternalServerErrorException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.INTERNAL_SERVER_ERROR);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidInternalServerErrorExceptionFromStatus() throws Exception
    {
        Device[] to = { VALID_D1 };
        // Don't run in debug mode because in debug mode the registration ids aren't checked.
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(
                    ResponseType.INTERNAL_SERVER_ERROR_FROM_STATUS);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidInternalServerErrorException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getFamily(), androidResponse.response.getStatusInfo()
                    .getFamily());
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), androidResponse.response
                    .getStatusInfo().getReasonPhrase());
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), androidResponse.response
                    .getStatusInfo().getStatusCode());
            validateUninitializedAndroidResponse(androidResponse);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidTimeoutExceptionFromResult() throws Exception
    {
        Device[] to = { VALID_D1 };
        // Don't run in debug mode because in debug mode the registration ids aren't checked.
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(
                    ResponseType.SERVER_TIMEOUT_FROM_RESULT);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidTimeoutException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.UNAVAILABLE);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidTimeoutExceptionFromStatus() throws Exception
    {
        Device[] to = { VALID_D1 };
        // Don't run in debug mode because in debug mode the registration ids aren't checked.
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(
                    ResponseType.SERVER_TIMEOUT_FROM_STATUS);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidTimeoutException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertThat(androidResponse.response.getStatusInfo().getStatusCode(), greaterThan(500));
            assertThat(androidResponse.response.getStatusInfo().getStatusCode(), lessThan(600));
            validateUninitializedAndroidResponse(androidResponse);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidMissingRegistrationIdExceptionFromResult() throws Exception
    {
        Device D2Temp = Device2.getDomainObject();
        D2Temp.setCode(null);
        Device[] to = { D2Temp };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(
                    ResponseType.MISSING_REGISTRATION_ID);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidMissingRegistrationIdException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.MISSING_REGISTRATION_ID);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidMissingRegistrationIdExceptionFromStatus() throws Exception
    {
        Device D2Temp = Device2.getDomainObject();
        D2Temp.setCode(null);
        Device[] to = { D2Temp };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidMissingRegistrationIdException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.BAD_REQUEST.getFamily(), androidResponse.response.getStatusInfo().getFamily());
            assertEquals(Response.Status.BAD_REQUEST.getReasonPhrase(), androidResponse.response.getStatusInfo()
                    .getReasonPhrase());
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), androidResponse.response.getStatusInfo()
                    .getStatusCode());
            validateUninitializedAndroidResponse(androidResponse);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidUnregisteredDeviceException() throws Exception
    {
        Device[] to = { UNREGISTERED_D4 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(ResponseType.UNREGISTERED_DEVICE);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidUnregisteredDeviceException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.UNREGISTERED_DEVICE);
            List<String> unregisteredDeviceIds = e.getUnregisteredDeviceIds();
            assertThat(unregisteredDeviceIds.size(), equalTo(1));
            assertThat(unregisteredDeviceIds.get(0), equalTo(UNREGISTERED_D4.getCode()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidInvalidRegistrationIdException() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidInvalidRegistrationIdException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.INVALID_REGISTRATION_ID);
            List<String> invalidRegistrationIds = e.getInvalidRegistrationIds();
            assertThat(invalidRegistrationIds.size(), equalTo(1));
            assertThat(invalidRegistrationIds.get(0), equalTo(D2.getCode()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidMessageTooBigException() throws Exception
    {
        Map<String, String> invalidMessageMap = new HashMap<>(MESSAGE_MAP);
        invalidMessageMap.put(MESSAGE5_KEY_TOO_BIG, MESSAGE5_VALUE_TOO_BIG);
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, invalidMessageMap);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidMessageTooBigException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.MESSAGE_TOO_BIG);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidInvalidDataKeyException() throws Exception
    {
        Map<String, String> invalidMessageMap = new HashMap<>();
        invalidMessageMap.put(MESSAGE3_KEY_INVALID_WORD, MESSAGE3_VALUE_INVALID_WORD);
        invalidMessageMap.put(MESSAGE4_KEY_INVALID_WORD, MESSAGE4_VALUE_INVALID_WORD);
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, invalidMessageMap);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(ResponseType.INVALID_DATA_KEY);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidInvalidDataKeyException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkWithFailedMessageAndErrorAndroidResponse(androidResponse, GCMErrorMessage.INVALID_DATA_KEY);
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_AndroidCanonicalIdException() throws Exception
    {
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            MockAndroidConnection mockAndroidConnection = new MockAndroidConnection(ResponseType.NEW_CANONICAL_ID);
            mockAndroidConnection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidCanonicalIdException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            validateOkAndroidResponse(androidResponse);
            assertThat(androidResponse.failedMessageCount, equalTo(0L));
            assertThat(androidResponse.canonicalRegistrationIdCount, equalTo(1L));
            assertThat(androidResponse.multicastId, greaterThan(0L));
            assertThat(androidResponse.successfulMessageCount, equalTo(1L));
            assertThat(androidResponse.results[0].error, nullValue());
            assertThat(androidResponse.results[0].messageId, notNullValue());
            assertThat(androidResponse.results[0].registrationId, notNullValue());
            Map<String, String> originalToCanonicalRegistrationIdMap = e.getOriginalToCanonicalRegistrationIdMap();
            assertThat(originalToCanonicalRegistrationIdMap.size(), equalTo(1));
            assertThat(originalToCanonicalRegistrationIdMap.keySet(), contains(VALID_D1.getCode()));
            assertThat(originalToCanonicalRegistrationIdMap.get(VALID_D1.getCode()), not(equalTo(VALID_D1.getCode())));
            assertThat(androidResponse.results[0].registrationId,
                    equalTo(originalToCanonicalRegistrationIdMap.get(VALID_D1.getCode())));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSendMessage_ValidRegistrationId() throws Exception
    {
        Device[] to = { VALID_D1 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        SendMessageResponse response = connection.sendMessage(request);
        assertTrue(response instanceof AndroidSendMessageResponse);
        AndroidSendMessageResponse androidResponse = (AndroidSendMessageResponse) response;
        assertThat(androidResponse.failedMessageCount, equalTo(0L));
        assertThat(androidResponse.canonicalRegistrationIdCount, equalTo(0L));
        assertThat(androidResponse.multicastId, greaterThan(0L));
        assertThat(androidResponse.successfulMessageCount, equalTo(1L));
        assertThat(androidResponse.results[0].error, nullValue());
        assertThat(androidResponse.results[0].messageId, notNullValue());
        assertThat(androidResponse.results[0].registrationId, nullValue());
    }

    @Test
    @Ignore
    /**
     * This should throw an AndroidMulticastException until I support multicast
     * 
     * @throws Exception
     */
    public void testSendMessage_ValidRegistrationId_Multiple() throws Exception
    {
        Device[] to = { VALID_D1, VALID_D2, VALID_D3 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        SendMessageResponse response = connection.sendMessage(request);
        assertTrue(response instanceof AndroidSendMessageResponse);
        AndroidSendMessageResponse androidResponse = (AndroidSendMessageResponse) response;
        assertThat(androidResponse.failedMessageCount, equalTo(0L));
        assertThat(androidResponse.canonicalRegistrationIdCount, equalTo(0L));
        assertThat(androidResponse.multicastId, greaterThan(0L));
        assertThat(androidResponse.successfulMessageCount, equalTo(3L));
    }

    @Test
    @Ignore
    /**
     * This should throw an AndroidMulticastException until I support multicast
     * 
     * @throws Exception
     */
    public void testSendMessage_MixOfValidAndInvalidRegistrationIDs() throws Exception
    {
        Device[] to = { D2, D3, D4, D5, D6, VALID_D1, VALID_D2, VALID_D3 };
        SendMessageRequest request = new SendMessageRequest(D1, to, MESSAGE_MAP);
        try {
            connection.sendMessage(request);
            fail("should not get here");
        } catch (AndroidSendMessageException e) {
            AndroidSendMessageResponse androidResponse = e.response;
            assertEquals(Response.Status.OK.getFamily(), androidResponse.response.getStatusInfo().getFamily());
            assertEquals(Response.Status.OK.getReasonPhrase(), androidResponse.response.getStatusInfo()
                    .getReasonPhrase());
            assertEquals(Response.Status.OK.getStatusCode(), androidResponse.response.getStatusInfo().getStatusCode());
            assertThat(androidResponse.failedMessageCount, equalTo(5L));
            assertThat(androidResponse.canonicalRegistrationIdCount, equalTo(0L));
            assertThat(androidResponse.multicastId, greaterThan(0L));
            assertThat(androidResponse.successfulMessageCount, equalTo(3L));
            for (int toIndex = 0; toIndex < to.length; toIndex++) {
                // The first 5 are invalid registrations, the next 3 are valid.
                if (toIndex < 5) {
                    assertThat(androidResponse.results[toIndex].getGCMErrorMessage(),
                            equalTo(AndroidSendMessageResponse.AndroidResult.GCMErrorMessage.INVALID_REGISTRATION_ID));
                    assertThat(androidResponse.results[toIndex].messageId, nullValue());
                    assertThat(androidResponse.results[toIndex].registrationId, isEmptyString());
                } else {
                    assertThat(androidResponse.results[toIndex].error, nullValue());
                    assertThat(androidResponse.results[toIndex].messageId, equalTo("fake_message_id"));
                    assertThat(androidResponse.results[toIndex].registrationId, isEmptyString());
                }
            }
            return;
        }
        fail("should not get here");
    }
}

