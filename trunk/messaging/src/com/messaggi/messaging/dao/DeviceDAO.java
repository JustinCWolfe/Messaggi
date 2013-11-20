package com.messaggi.messaging.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.messaggi.dao.DAOException;
import com.messaggi.dao.PersistManager;
import com.messaggi.dao.PersistManager.Delete;
import com.messaggi.dao.PersistManager.Insert;
import com.messaggi.dao.PersistManager.Select;
import com.messaggi.dao.PersistManager.Update;
import com.messaggi.messaging.domain.Device;

public class DeviceDAO implements Insert<Device>, Select<Device>, Update<Device>, Delete<Device>
{
    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_device()}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, Device domainObject)
        throws SQLException
    {
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, Device domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<Device> prototypes) throws DAOException
    {
        return "{call m_get_device_by_id(?)}";
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, Device domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, Device domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return "{call m_update_device(?,?)}";
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, Device domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setBoolean(2, domainObject.getActive());
    }

    // Delete implementation
    @Override
    public String getDeleteStoredProcedure()
    {
        return "{call m_inactivate_device_by_id(?)}";
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObject(PreparedStatement stmt, Device domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    public List<Device> insertDevice(List<Device> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    public List<Device> selectDevice(List<Device> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    public void updateDevice(List<Device> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }

    public void deleteDevice(List<Device> prototypes) throws DAOException
    {
        PersistManager.delete(this, prototypes);
    }
}
