package com.messaggi.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.messaggi.dao.PersistManager.Delete;
import com.messaggi.dao.PersistManager.Insert;
import com.messaggi.dao.PersistManager.Select;
import com.messaggi.dao.PersistManager.Update;
import com.messaggi.domain.Device;

public class DeviceDAO implements Insert<Device>, Select<Device>, Update<Device>, Delete<Device>
{
    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_device()}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Device> domainObjects)
        throws SQLException
    {
    }

    @Override
    public void afterInsertInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects)
        throws SQLException
    {
        for (Device domainObject : domainObjects) {
            //domainObject.setId(rs.getLong("id"));
            //domainObject.setActive(rs.getBoolean("active"));
        }
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<Device> prototypes) throws SQLException
    {
        return "{call m_get_device_by_id(?)}";
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Device> domainObjects)
        throws SQLException
    {
        for (Device domainObject : domainObjects) {
            //stmt.setLong(1, domainObject.getId());
        }
    }

    @Override
    public void afterSelectInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects)
        throws SQLException
    {
        for (Device domainObject : domainObjects) {
            //domainObject.setId(rs.getLong("id"));
            //domainObject.setActive(rs.getBoolean("active"));
        }
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return "{call m_update_device(?,?)}";
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Device> domainObjects)
        throws SQLException
    {
        for (Device domainObject : domainObjects) {
            //stmt.setLong(1, domainObject.getId());
            //stmt.setBoolean(2, domainObject.getActive());
        }
    }

    @Override
    public void afterUpdateInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects)
        throws SQLException
    {
    }

    // Delete implementation
    @Override
    public String getDeleteStoredProcedure()
    {
        return "{call m_inactivate_device_by_id(?)}";
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Device> domainObjects)
        throws SQLException
    {
        for (Device domainObject : domainObjects) {
            //stmt.setLong(1, domainObject.getId());
        }
    }

    @Override
    public void afterDeleteInitializeDomainObjectsFromResultSet(ResultSet rs, List<Device> domainObjects)
        throws SQLException
    {
    }

    public List<Device> insertDevice(List<Device> newVersions) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        return PersistManager.insert(this, newVersions);
    }

    public List<Device> selectDevice(List<Device> prototypes) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        return PersistManager.select(this, prototypes);
    }

    public void updateDevice(List<Device> newVersions) throws NamingException, SQLException
    {
        PersistManager.update(this, newVersions);
    }

    public void deleteDevice(List<Device> prototypes) throws NamingException, SQLException
    {
        PersistManager.delete(this, prototypes);
    }
}
