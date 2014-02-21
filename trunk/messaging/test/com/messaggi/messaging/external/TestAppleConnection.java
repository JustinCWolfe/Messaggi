package com.messaggi.messaging.external;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.TestDataHelper.ApplicationPlatformAppleTesting;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.external.MessagingServiceConnectionFactory;
import com.messaggi.junit.MessaggiTestCase;

public class TestAppleConnection extends MessaggiTestCase
{
    private static MessagingServiceConnection appleConnection;

    private static final ApplicationPlatform APP_PLAT = ApplicationPlatformAppleTesting.getDomainObject();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        appleConnection = new MockAppleConnection();
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
    public void test()
    {
        fail("Not yet implemented");
    }
}

