package com.messaggi.junit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;

public abstract class MessaggiTestCase
{
    private static final String DEFAULT_LOG4J_FILE_NAME = "log4j.properties";

    private static void configureLogging()
    {
        configureLogging(DEFAULT_LOG4J_FILE_NAME);
    }

    private static void configureLogging(String propertyFile)
    {
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(propertyFile);
    }

    private MockAppender mockAppender = new MockAppender();

    public MockAppender getMockAppender()
    {
        return mockAppender;
    }

    public MessaggiTestCase()
    {
        configureLogging();
    }

    public MessaggiTestCase(String loggingPropertyFile)
    {
        configureLogging(loggingPropertyFile);
    }

    @Before
    public void setUp() throws Exception
    {
        // Add mock appender to root logger so we can capture all log messages.
        Logger.getRootLogger().addAppender(mockAppender);
        mockAppender.clearLogEvents();
    }

    @After
    public void tearDown() throws Exception
    {
        Logger.getRootLogger().removeAppender(mockAppender);
    }
}

