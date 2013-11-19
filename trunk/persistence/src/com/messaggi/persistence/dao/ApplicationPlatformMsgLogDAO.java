package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.domain.ApplicationPlatformMsgLog;

public interface ApplicationPlatformMsgLogDAO
{
    List<ApplicationPlatformMsgLog> insertApplicationPlatformMsgLog(List<ApplicationPlatformMsgLog> newVersions)
        throws DAOException;

    List<ApplicationPlatformMsgLog> insertApplicationPlatformMsgLog(List<ApplicationPlatformMsgLog> newVersions,
            Connection conn, EnumSet<Select.Option> options) throws DAOException;
}

