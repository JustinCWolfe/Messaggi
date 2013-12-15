package com.messaggi.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import com.messaggi.dao.PersistManager.Get;
import com.messaggi.dao.PersistManager.Save;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;

public class DeviceDAO implements Get<Device>, Save<Device>
{
    private void initializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects) throws SQLException
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
    public String getGetStoredProcedure(List<Device> prototypes) throws SQLException
    {
        return "{call GetDevice(?)}";
    }

    @Override
    public void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects)
        throws SQLException
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
        throws SQLException
    {
        initializeDomainObjectsFromResultSet(rs, domainObjects);
    }

    public List<Device> getDevice(List<Device> prototypes) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException,
        JAXBException, IOException
    {
        return PersistManager.get(this, prototypes);
    }

    public List<Device> saveDevice(List<Device> newVersions) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException,
        JAXBException, IOException
    {
        return PersistManager.save(this, newVersions);
    }
}
