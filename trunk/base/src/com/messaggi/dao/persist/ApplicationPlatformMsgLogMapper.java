package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatformMsgLog;

public class ApplicationPlatformMsgLogMapper implements Save<ApplicationPlatformMsgLog>
{
    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveApplicationPlatformMsgLog(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs,
            List<ApplicationPlatformMsgLog> domainObjects) throws Exception
    {
        while (rs.next()) {
            ApplicationPlatformMsgLog domainObject = new ApplicationPlatformMsgLog();
            domainObject.setId(rs.getInt("ID"));
            domainObject.setDate(rs.getDate("Date"));
            domainObject.setMsgCount(rs.getInt("MsgCount"));
            ApplicationPlatform applicationPlatform = new ApplicationPlatform();
            applicationPlatform.setId(rs.getInt("ApplicationPlatformID"));
            domainObject.setApplicationPlatform(applicationPlatform);
            domainObjects.add(domainObject);
        }
    }
}

