package com.messaggi.messaging.pool;

import javax.naming.InitialContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Application1;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestSendMessageThreadPool extends MessaggiTestCase
{
    private Application app1;

    private ApplicationPlatform appPlat1;

    private ApplicationPlatform appPlat2;

    private ApplicationPlatform appPlat3;

    private Device d1;

    private Device d2;

    private Device d3;

    private Device d4;

    private Device d5;

    private Device d6;

    private User user1;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        messaggiSuiteSetUp();
        // Implementation class name for send message thread pool.
        InitialContext ic = new InitialContext();
        ic.bind("messaggi:/pool/SendMessageThreadPool", new SendMessageThreadPoolImpl());
    }

    @AfterClass
    public static void tearDownAfterClassClass() throws Exception
    {
        messaggiSuiteTearDown();
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        user1 = User1.getDomainObject();
        TestDataHelper.createUser(user1);
        app1 = Application1.getDomainObject();
        app1.setUser(user1);
        TestDataHelper.createApplication(app1);
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        TestDataHelper.createApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteDevice(d1);
        TestDataHelper.deleteDevice(d2);
        TestDataHelper.deleteDevice(d3);
        TestDataHelper.deleteDevice(d4);
        TestDataHelper.deleteDevice(d5);
        TestDataHelper.deleteDevice(d6);
        TestDataHelper.deleteApplicationPlatform(appPlat1);
        TestDataHelper.deleteApplicationPlatform(appPlat2);
        TestDataHelper.deleteApplicationPlatform(appPlat3);
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteUser(user1);
        super.tearDown();
    }

    @Test
    public void test1() throws Exception
    {
        // Do we need items in database for these tests - I don't think so.
        SendMessages.sendMessage(null, null, null, null);
    }

    @Test
    public void test2() throws Exception
    {
        // Do we need items in database for these tests - I don't think so.
        SendMessages.sendMessage(null, null, null, null);
    }
}

