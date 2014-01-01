package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
        HashMap<Integer, ApplicationPlatform> applicationPlatformMap = new HashMap<>();
        while (rs.next()) {
            ApplicationPlatformMsgLog domainObject = new ApplicationPlatformMsgLog();

            int id = rs.getInt("ID");
            domainObject.setId(rs.wasNull() ? null : id);

            domainObject.setDate(new Date(rs.getLong("Date")));

            int msgCount = rs.getInt("MsgCount");
            domainObject.setMsgCount(rs.wasNull() ? null : msgCount);

            int applicationPlatformId = rs.getInt("ApplicationPlatformID");
            Integer applicationPlatformIdObj = rs.wasNull() ? null : applicationPlatformId;
            if (applicationPlatformIdObj != null) {
                ApplicationPlatform applicationPlatform = null;
                if (!applicationPlatformMap.containsKey(applicationPlatformIdObj)) {
                    applicationPlatform = new ApplicationPlatform();
                    applicationPlatform.setId(applicationPlatformIdObj);
                    applicationPlatform.setApplicationPlatformMsgLogs(new HashSet<ApplicationPlatformMsgLog>());
                    applicationPlatformMap.put(applicationPlatformIdObj, applicationPlatform);
                } else {
                    applicationPlatform = applicationPlatformMap.get(applicationPlatformIdObj);
                }
                applicationPlatform.getApplicationPlatformMsgLogs().add(domainObject);
                domainObject.setApplicationPlatform(applicationPlatform);
            }

            domainObjects.add(domainObject);
        }
    }
}

