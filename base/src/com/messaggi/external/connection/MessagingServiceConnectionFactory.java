package com.messaggi.external.connection;

import com.messaggi.domain.ApplicationPlatform;

public interface MessagingServiceConnectionFactory
{
    MessagingServiceConnection create(ApplicationPlatform applicationPlatform);
}

