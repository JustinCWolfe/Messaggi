package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.messages.SendMessageRequest;

public interface MessagingServiceConnection extends Runnable
{
    public static class ConnectionFactory
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

    void setApplicationPlatform(ApplicationPlatform applicationPlatform);

    ApplicationPlatform getApplicationPlatform();

    void connect();
    
    void setSendMessageRequest(SendMessageRequest request);

    @Override
    void run();
}

