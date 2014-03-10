package com.messaggi.messaging.external;

import com.notnoop.apns.ApnsServiceBuilder;

public class MockAppleConnection extends AppleConnection
{
    @Override
    protected void setBuilderDestination(ApnsServiceBuilder builder)
    {
        builder.withSandboxDestination();
    }
}

