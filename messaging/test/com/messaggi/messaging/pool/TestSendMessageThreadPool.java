package com.messaggi.messaging.pool;

import static org.junit.Assert.fail;

import javax.naming.InitialContext;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.junit.MessaggiTestCase;

public class TestSendMessageThreadPool extends MessaggiTestCase
{
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        InitialContext ic = new InitialContext();
        ic.bind("java:/comp/env/SendMessageThreadPoolImpl", "com.messaggi.messaging.pool.SendMessageThreadPoolImpl");
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void test()
    {
        fail("Not yet implemented");
    }
}

