package com.messaggi.messaging.external;

public class MockAppleConnection extends AppleConnection
{
    static {
        AppleConnection.APPLE_PUSH_NOTIFICATION_HOST = "gateway.sandbox.push.apple.com";
    }
}

