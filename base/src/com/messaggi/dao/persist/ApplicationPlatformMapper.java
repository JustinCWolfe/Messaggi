package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatform.Platform;

public class ApplicationPlatformMapper implements Get<ApplicationPlatform>, Save<ApplicationPlatform>
{
    private void initializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws Exception
    {
        while (rs.next()) {
            ApplicationPlatform domainObject = new ApplicationPlatform();
            domainObject.setId(rs.getInt("ID"));
            domainObject.setPlatform(Platform.valueOf(Platform.class, rs.getString("PlatformCode")));
            domainObject.setToken(UUID.fromString(rs.getString("Token")));
            Application application = new Application();
            application.setId(rs.getInt("ApplicationID"));
            domainObject.setApplication(application);
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

