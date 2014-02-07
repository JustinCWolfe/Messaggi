package com.messaggi.messaging.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;

public class MessagingServiceConnectionFactory
{
    public static MessagingServiceConnection create(ApplicationPlatform applicationPlatform)
    {
        MessagingServiceConnection messagingConnection = null;
        switch (applicationPlatform.getPlatform()) {
            case AMAZON:
                messagingConnection = new AmazonConnection();
                break;
            case ANDROID:
                messagingConnection = new AndroidConnection();
                break;
            case IOS:
                messagingConnection = new AppleConnection();
                break;
            case WINDOWS:
                messagingConnection = new WindowsConnection();
                break;
            default:
                throw new UnsupportedOperationException();
        }
        messagingConnection.setApplicationPlatform(applicationPlatform);
        return messagingConnection;
    }
}

