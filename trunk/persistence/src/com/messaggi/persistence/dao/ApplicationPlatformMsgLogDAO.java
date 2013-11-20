package com.messaggi.persistence.dao;

import java.util.List;

import com.messaggi.persistence.domain.ApplicationPlatformMsgLog;

public interface ApplicationPlatformMsgLogDAO
{
    List<ApplicationPlatformMsgLog> insertApplicationPlatformMsgLog(List<ApplicationPlatformMsgLog> newVersions)
        throws DAOException;
}

