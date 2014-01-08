package com.messaggi.dao;

import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper;
import com.messaggi.dao.persist.ObjectRelationalMapper.Factory.DomainObjectType;
import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.GetAll;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.ApplicationPlatform;

public class ApplicationPlatformDAO
{
    public List<ApplicationPlatform> getApplicationPlatform(ApplicationPlatform[] prototypes) throws Exception
    {
        Get<ApplicationPlatform> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.ApplicationPlatform);
        return PersistManager.get(mapper, prototypes);
    }

    public List<ApplicationPlatform> getAllApplicationPlatformIdsAndTokens() throws Exception
    {
        GetAll<ApplicationPlatform> mapper = ObjectRelationalMapper.Factory
                .create(DomainObjectType.ApplicationPlatform);
        return PersistManager.getAll(mapper);
    }

    public List<ApplicationPlatform> saveApplicationPlatform(ApplicationPlatform[] newVersions) throws Exception
    {
        Save<ApplicationPlatform> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.ApplicationPlatform);
        return PersistManager.save(mapper, newVersions);
    }
}

