package com.messaggi.dao;

import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper;
import com.messaggi.dao.persist.ObjectRelationalMapper.Factory.DomainObjectType;
import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.User;

public class UserDAO
{
    public List<User> getUser(User[] prototypes) throws Exception
    {
        Get<User> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.User);
        return PersistManager.get(mapper, prototypes);
    }

    public List<User> saveUser(User[] newVersions) throws Exception
    {
        Save<User> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.User);
        return PersistManager.save(mapper, newVersions);
    }
}
