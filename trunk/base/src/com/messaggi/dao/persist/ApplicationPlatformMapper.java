package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.GetAll;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatform.Platform;

public class ApplicationPlatformMapper implements Get<ApplicationPlatform>, GetAll<ApplicationPlatform>,
        Save<ApplicationPlatform>
{
    private void initializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws Exception
    {
        HashMap<Integer, Application> applicationMap = new HashMap<>();
        while (rs.next()) {
            ApplicationPlatform domainObject = new ApplicationPlatform();

            int id = rs.getInt("ID");
            domainObject.setId(rs.wasNull() ? null : id);

            domainObject.setPlatform(Platform.valueOf(Platform.class, rs.getString("PlatformCode")));
            domainObject.setToken(UUID.fromString(rs.getString("Token")));

            byte[] externalServiceToken = rs.getBytes("ExternalServiceToken");
            domainObject.setExternalServiceTokenAsBinary(rs.wasNull() ? null : externalServiceToken);

            byte[] externalServicePassword = rs.getBytes("ExternalServicePassword");
            domainObject.setExternalServicePasswordAsBinary(rs.wasNull() ? null : externalServicePassword);

            int applicationId = rs.getInt("ApplicationID");
            Integer applicationIdObj = rs.wasNull() ? null : applicationId;
            if (applicationIdObj != null) {
                Application application = null;
                if (!applicationMap.containsKey(applicationIdObj)) {
                    application = new Application();
                    application.setId(applicationIdObj);
                    application.setApplicationPlatforms(new HashSet<ApplicationPlatform>());
                    applicationMap.put(applicationIdObj, application);
                } else {
                    application = applicationMap.get(applicationIdObj);
                }
                application.getApplicationPlatforms().add(domainObject);
                domainObject.setApplication(application);
            }

            domainObjects.add(domainObject);
        }
    }

    // Get implementation
    @Override
    public String getGetStoredProcedure()
    {
        return "{call GetApplicationPlatform(?)}";
    }

    @Override
    public void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws Exception
    {
        initializeDomainObjectsFromResultSet(rs, domainObjects);
    }

    // Get all implementation
    @Override
    public String getGetAllStoredProcedure()
    {
        return "{call GetAllApplicationPlatformIdsAndTokens()}";
    }

    @Override
    public void afterGetAllInitializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws Exception
    {
        while (rs.next()) {
            ApplicationPlatform domainObject = new ApplicationPlatform();
            int id = rs.getInt("ID");
            domainObject.setId(rs.wasNull() ? null : id);
            domainObject.setToken(UUID.fromString(rs.getString("Token")));
            domainObjects.add(domainObject);
        }
    }

    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveApplicationPlatform(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws Exception
    {
        initializeDomainObjectsFromResultSet(rs, domainObjects);
    }
}

