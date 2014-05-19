package com.messaggi.messaging.workflow;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
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

    @Test
    public void test()
    {
        fail("Not yet implemented");
    }
}

