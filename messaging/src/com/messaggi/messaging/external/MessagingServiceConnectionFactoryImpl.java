package com.messaggi.messaging.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.external.MessagingServiceConnectionFactory;

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
            default:
                throw new UnsupportedOperationException();
        }
        messagingConnection.setApplicationPlatform(applicationPlatform);
        return messagingConnection;
    }
}

