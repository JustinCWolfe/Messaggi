package com.messaggi.junit;

import org.junit.After;
import org.junit.Before;

public abstract class WebServiceTestCase extends MessaggiTestCase
{
    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}

