package com.messaggi.workflow;

import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessagingWorkflows
{
    private static final String FACTORY_JNDI_NAME = "messaggi:/factory/MessagingWorkflowFactory";

    private static final MessagingWorkflowFactory factory;

    static {
        try {
            factory = (MessagingWorkflowFactory) InitialContext.doLookup(FACTORY_JNDI_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Could not find " + FACTORY_JNDI_NAME, e);
        }
    }

    public static MessagingWorkflow create(UUID applicationPlatformToken) throws Exception
    {
        return factory.create(applicationPlatformToken);
    }
}

