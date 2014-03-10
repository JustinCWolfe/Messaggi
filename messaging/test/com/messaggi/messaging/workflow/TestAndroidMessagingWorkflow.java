package com.messaggi.messaging.workflow;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.junit.MessaggiTestCase;

public class TestAndroidMessagingWorkflow extends MessaggiTestCase
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
    public void testSendMessage_AndroidInternalServerErrorException_ExponentialBackOff() throws Exception
    {
        // Errors in the 500-599 range (such as 500 or 503) indicate that there was an internal error 
        // in the GCM server while trying to process the request, or that the server is temporarily 
        // unavailable (for example, because of timeouts). Sender must retry later, honoring any 
        // Retry-After header included in the response. Application servers must implement exponential back-off.
        fail("figure out how to test the exponential back-off stuff - will need to mock the gcm service for this");
    }

    @Test
    public void testSendMessage_AndroidTimeoutException_ExponentialBackOff() throws Exception
    {
        // Errors in the 500-599 range (such as 500 or 503) indicate that there was an internal error 
        // in the GCM server while trying to process the request, or that the server is temporarily 
        // unavailable (for example, because of timeouts). Sender must retry later, honoring any 
        // Retry-After header included in the response. Application servers must implement exponential back-off.
        fail("figure out how to test the exponential back-off stuff - will need to mock the gcm service for this");
    }

    @Test
    public void testSendMessage_AndroidUnknownException_ExponentialBackOff() throws Exception
    {
        // Errors in the 500-599 range (such as 500 or 503) indicate that there was an internal error 
        // in the GCM server while trying to process the request, or that the server is temporarily 
        // unavailable (for example, because of timeouts). Sender must retry later, honoring any 
        // Retry-After header included in the response. Application servers must implement exponential back-off.
        fail("figure out how to test the exponential back-off stuff - will need to mock the gcm service for this");
    }
}

