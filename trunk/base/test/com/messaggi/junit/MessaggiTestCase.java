package com.messaggi.junit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

public class MessaggiTestCase
{
    private final Log log = LogFactory.getLog(MessaggiTestCase.class);

    private MockAppender mockAppender = new MockAppender();

    public MockAppender getMockAppender()
    {
        return mockAppender;
    }

    @Before
    public void setUp() throws Exception
    {
        // Add mock appender to root logger so we can capture all log messages.
        //log.getRootLogger().addAppender(mockAppender);
        mockAppender.clearLogEvents();
    }

    @After
    public void tearDown() throws Exception
    {
        //log.getRootLogger().removeAppender(mockAppender);
    }
}

