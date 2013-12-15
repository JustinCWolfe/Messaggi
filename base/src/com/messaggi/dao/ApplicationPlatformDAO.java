package com.messaggi.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import com.messaggi.dao.PersistManager.Get;
import com.messaggi.dao.PersistManager.Save;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatform.Platform;

public class ApplicationPlatformDAO implements Get<ApplicationPlatform>, Save<ApplicationPlatform>
{
    private void initializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws SQLException
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
    public String getGetStoredProcedure(List<ApplicationPlatform> prototypes) throws SQLException
    {
        return "{call GetApplicationPlatform(?)}";
    }

    @Override
    public void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<ApplicationPlatform> domainObjects)
        throws SQLException
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
        throws SQLException
    {
        initializeDomainObjectsFromResultSet(rs, domainObjects);
    }

    public List<ApplicationPlatform> getApplicationPlatform(List<ApplicationPlatform> prototypes)
        throws NamingException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException,
        InstantiationException, JAXBException, IOException
    {
        return PersistManager.get(this, prototypes);
    }

    public List<ApplicationPlatform> saveApplicationPlatform(List<ApplicationPlatform> newVersions)
        throws NamingException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException,
        InstantiationException, JAXBException, IOException
    {
        return PersistManager.save(this, newVersions);
    }
}

