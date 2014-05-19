package com.messaggi.messaging.external.connection;

import com.messaggi.messaging.external.connection.AppleConnection;
import com.notnoop.apns.ApnsServiceBuilder;

public class MockAppleConnection extends AppleConnection
{
    @Override
    protected void setBuilderDestination(ApnsServiceBuilder builder)
    {
        builder.withSandboxDestination();
    }
}

