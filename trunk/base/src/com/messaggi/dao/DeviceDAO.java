package com.messaggi.dao;

import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper;
import com.messaggi.dao.persist.ObjectRelationalMapper.Factory.DomainObjectType;
import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.Device;

public class DeviceDAO
{
    public List<Device> getDevice(Device[] prototypes) throws Exception
    {
        Get<Device> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.Device);
        return PersistManager.get(mapper, prototypes);
    }

    public List<Device> saveDevice(Device[] newVersions) throws Exception
    {
        Save<Device> mapper = ObjectRelationalMapper.Factory.create(DomainObjectType.Device);
        return PersistManager.save(mapper, newVersions);
    }
}
