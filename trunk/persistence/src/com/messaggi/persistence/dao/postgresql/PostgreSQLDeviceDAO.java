package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.DeviceDAO;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Delete;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.PersistManager.Update;
import com.messaggi.persistence.domain.Device;

public class PostgreSQLDeviceDAO implements DeviceDAO, Insert<Device>, Select<Device>, Update<Device>, Delete<Device>
{
    // Persist implementation
    @Override
    public Connection createConnection() throws SQLException
    {
        return PostgreSQLDAOFactory.createConnection();
    }

    @Override
    public String getDomainObjectIdentifier(Device domainObject)
    {
        return domainObject.getId().toString();
    }

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
        stmt.setBoolean(4, domainObject.getActive());
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

    @Override
    public List<Device> insertDevice(List<Device> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<Device> insertDevice(List<Device> newVersions, Connection conn) throws DAOException
    {
        return PersistManager.insert(this, newVersions, conn);
    }

    @Override
    public List<Device> selectDevice(List<Device> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public List<Device> selectDevice(List<Device> prototypes, Connection conn) throws DAOException
    {
        return PersistManager.select(this, prototypes, conn);
    }

    @Override
    public void updateDevice(List<Device> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }

    @Override
    public void updateDevice(List<Device> newVersions, Connection conn) throws DAOException
    {
        PersistManager.update(this, newVersions, conn);
    }

    @Override
    public void deleteDevice(List<Device> prototypes) throws DAOException
    {
        PersistManager.delete(this, prototypes);
    }

    @Override
    public void deleteDevice(List<Device> prototypes, Connection conn) throws DAOException
    {
        PersistManager.delete(this, prototypes, conn);
    }
}