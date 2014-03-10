package com.messaggi.junit;

public abstract class WebServiceTestCase extends MessaggiTestCase
{
    public static void webServiceSuiteSetUp() throws Exception
    {
        messaggiSuiteSetUp();
    }

    public static void webServiceSuiteTearDown() throws Exception
    {
        messaggiSuiteTearDown();
    }
}

