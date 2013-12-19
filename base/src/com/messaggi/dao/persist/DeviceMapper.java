package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class DeviceMapper implements Get<Device>, Save<Device>
{
    private void initializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects) throws Exception
    {
        while (rs.next()) {
            Device domainObject = new Device();
            domainObject.setCode(rs.getString("Code"));
            domainObject.setActive(rs.getBoolean("Active"));
            ApplicationPlatform applicationPlatform = new ApplicationPlatform();
            applicationPlatform.setId(rs.getInt("ApplicationPlatformID"));
            HashSet<ApplicationPlatform> applicationPlatforms = new HashSet<>();
            applicationPlatforms.add(applicationPlatform);
            domainObject.setApplicationPlatforms(applicationPlatforms);
            domainObjects.add(domainObject);
        }
    }

    // Get implementation
    @Override
    public String getGetStoredProcedure()
    {
        return "{call GetDevice(?)}";
    }

    @Override
    public void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects) throws Exception
    {
        initializeDomainObjectsFromResultSet(rs, domainObjects);
    }

    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveDevice(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects)
        throws Exception
    {
        initializeDomainObjectsFromResultSet(rs, domainObjects);
    }
}

