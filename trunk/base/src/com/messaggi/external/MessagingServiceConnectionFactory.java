package com.messaggi.external;

import com.messaggi.domain.ApplicationPlatform;

public interface MessagingServiceConnectionFactory
{
    MessagingServiceConnection create(ApplicationPlatform applicationPlatform);
}

