package com.messaggi.messaging.external;

import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
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
        SendMessageRequest smr = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        connection.sendMessage(smr);
    }

    @Test
    public void testSendMessage_InvalidRegistrationIDKey() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest smr = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        androidConnection.sendMessage(smr);
    }

    @Test
    public void testSendMessage_SingleRegistrationID() throws Exception
    {
        Device[] to = { D2 };
        SendMessageRequest smr = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        androidConnection.sendMessage(smr);
    }

    @Test
    public void testSendMessage_MultipleRegistrationIDs() throws Exception
    {
        Device[] to = { D2, D3, D4, D5, D6 };
        SendMessageRequest smr = new SendMessageRequest(D1, to, MESSAGE1_TEXT, true);
        androidConnection.sendMessage(smr);
    }
}

