package com.messaggi.messaging.external;

import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.TestDataHelper.Device1;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.Device3;
import com.messaggi.TestDataHelper.Device4;
import com.messaggi.TestDataHelper.Device5;
import com.messaggi.TestDataHelper.Device6;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatform.Platform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.external.MessagingServiceConnectionFactory;
import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.messages.SendMessageRequest;

public class TestAndroidConnection extends MessaggiTestCase
{
    private static final String MESSAGE1_TEXT = "Some message text";

    private static final String MESSAGE2_TEXT = "Some other message text";

    private static final ApplicationPlatform APP_PLAT = new ApplicationPlatform();

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
        APP_PLAT.setPlatform(Platform.ANDROID);
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
    public void testSendMessage()
    {
        //Content-Type:application/json
        //Authorization:key=AIzaSyB-1uEai2WiUapxCs2Q0GZYzPu7Udno5aA
        //{
        //  "registration_ids" : ["APA91bHun4MxP5egoKMwt2KZFBaFUH-1RYqx..."],
        //  "data" : {
        //    ...
        //  },
        //}
        Device[] to = new Device[] { D2, D3 };
        SendMessageRequest smr = new SendMessageRequest(D1, to, MESSAGE1_TEXT);
        androidConnection.sendMessage(smr);
    }
}

