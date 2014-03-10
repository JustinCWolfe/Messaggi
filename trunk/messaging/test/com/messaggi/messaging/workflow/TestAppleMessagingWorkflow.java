package com.messaggi.messaging.workflow;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.junit.MessaggiTestCase;

public class TestAppleMessagingWorkflow extends MessaggiTestCase
{
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        messaggiSuiteSetUp();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        messaggiSuiteTearDown();
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
    public void test()
    {
        fail("Not yet implemented");
    }
}

