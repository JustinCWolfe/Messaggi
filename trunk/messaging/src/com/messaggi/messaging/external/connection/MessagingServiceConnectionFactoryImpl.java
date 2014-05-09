package com.messaggi.messaging.external.connection;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.connection.MessagingServiceConnectionFactory;

public class MessagingServiceConnectionFactoryImpl implements MessagingServiceConnectionFactory
{
    @Override
    public MessagingServiceConnection create(ApplicationPlatform applicationPlatform)
    {
        MessagingServiceConnection messagingConnection = null;
        switch (applicationPlatform.getPlatform()) {
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

