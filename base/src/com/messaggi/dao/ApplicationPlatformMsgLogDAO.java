package com.messaggi.dao;

import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper;
import com.messaggi.dao.persist.ObjectRelationalMapper.Factory.DomainObjectType;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.ApplicationPlatformMsgLog;

public class ApplicationPlatformMsgLogDAO
{
    public List<ApplicationPlatformMsgLog> saveApplicationPlatformMsgLog(ApplicationPlatformMsgLog[] newVersions)
        throws Exception
    {
        Save<ApplicationPlatformMsgLog> mapper = ObjectRelationalMapper.Factory
                .create(DomainObjectType.ApplicationPlatformMsgLog);
        return PersistManager.save(mapper, newVersions);
    }
}

