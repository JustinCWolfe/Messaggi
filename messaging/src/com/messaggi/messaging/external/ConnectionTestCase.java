package com.messaggi.messaging.external;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import com.messaggi.TestDataHelper.Device1;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.Device3;
import com.messaggi.TestDataHelper.Device4;
import com.messaggi.TestDataHelper.Device5;
import com.messaggi.TestDataHelper.Device6;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.junit.MessaggiTestCase;

public abstract class ConnectionTestCase extends MessaggiTestCase
{
    protected static ApplicationPlatform APP_PLAT;

    protected static MessagingServiceConnection connection;

    protected static final Map<String, String> MESSAGE_MAP = new HashMap<>();

    protected static final Device D1 = Device1.getDomainObject();

    protected static final Device D2 = Device2.getDomainObject();

    protected static final Device D3 = Device3.getDomainObject();

    protected static final Device D4 = Device4.getDomainObject();

    protected static final Device D5 = Device5.getDomainObject();

    protected static final Device D6 = Device6.getDomainObject();

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

    public static void connectionSuiteSetUp() throws Exception
    {
        messaggiSuiteSetUp();
    }

    public static void connectionSuiteTearDown() throws Exception
    {
        messaggiSuiteTearDown();
    }
}

