package com.messaggi.dao;

import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper;
import com.messaggi.dao.persist.ObjectRelationalMapper.Factory.DomainObjectType;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.Application;

public class ApplicationDAO
{
    public List<Application> saveApplication(Application[] newVersions) throws Exception
    {
        Save<Application> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.Application);
        return PersistManager.save(mapper, newVersions);
    }
}
