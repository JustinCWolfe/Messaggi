package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;

public abstract class MessagingServiceConnection
{
    public static class ConnectionFactory
    {
        public static MessagingServiceConnection create(ApplicationPlatform ap)
        {
            switch (ap.getPlatform()) {
                case AMAZON:
                    return new AmazonConnection(ap);
                case ANDROID:
                    return new AmazonConnection(ap);
                case IOS:
                    return new AmazonConnection(ap);
                case WINDOWS:
                    return new AmazonConnection(ap);
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private ApplicationPlatform applicationPlatform;

    public ApplicationPlatform getApplicationPlatform()
    {
        return this.applicationPlatform;
    }

    public MessagingServiceConnection(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    public abstract void connect();
}

