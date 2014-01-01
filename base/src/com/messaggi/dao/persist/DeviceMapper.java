package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.HashMap;
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
        HashMap<String, Device> deviceMap = new HashMap<>();
        HashMap<Integer, ApplicationPlatform> applicationPlatformMap = new HashMap<>();
        while (rs.next()) {
            Device domainObject = null;
            String code = rs.getString("Code");
            if (!deviceMap.containsKey(code)) {
                domainObject = new Device();
                domainObject.setCode(code);

                boolean active = rs.getBoolean("Active");
                domainObject.setActive(rs.wasNull() ? null : active);

                domainObject.setApplicationPlatforms(new HashSet<ApplicationPlatform>());
                deviceMap.put(code, domainObject);
            } else {
                domainObject = deviceMap.get(code);
            }
            
            int applicationPlatformId = rs.getInt("ApplicationPlatformID");
            Integer applicationPlatformIdObj = rs.wasNull() ? null : applicationPlatformId;
            if (applicationPlatformIdObj != null) {
                ApplicationPlatform applicationPlatform = null;
                if (!applicationPlatformMap.containsKey(applicationPlatformIdObj)) {
                    applicationPlatform = new ApplicationPlatform();
                    applicationPlatform.setId(applicationPlatformIdObj);
                    applicationPlatform.setDevices(new HashSet<Device>());
                    applicationPlatformMap.put(applicationPlatformIdObj, applicationPlatform);
                } else {
                    applicationPlatform = applicationPlatformMap.get(applicationPlatformIdObj);
                }
                applicationPlatform.getDevices().add(domainObject);
                domainObject.getApplicationPlatforms().add(applicationPlatform);
            }
        }
        domainObjects.addAll(deviceMap.values());
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

