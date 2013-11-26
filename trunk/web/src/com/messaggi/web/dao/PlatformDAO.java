package com.messaggi.web.dao;

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
import com.messaggi.web.domain.Platform;
import com.messaggi.web.domain.Platform.PlatformServiceName;

public class PlatformDAO implements Insert<Platform>, Select<Platform>, Update<Platform>, Delete<Platform>
{
    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_platform(?,?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, Platform domainObject)
        throws SQLException
    {
        stmt.setString(1, domainObject.getName());
        stmt.setString(2, domainObject.getServiceName().toString());
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, Platform domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setServiceName(PlatformServiceName.valueOf(rs.getString("service_name")));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<Platform> prototypes) throws DAOException
    {
        return "{call m_get_platform_by_id(?)}";
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, Platform domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, Platform domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setServiceName(PlatformServiceName.valueOf(rs.getString("service_name")));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return "{call m_update_platform(?,?,?,?)}";
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, Platform domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setString(2, domainObject.getName());
        stmt.setString(3, domainObject.getServiceName().toString());
        stmt.setBoolean(4, domainObject.getActive());
    }

    // Delete implementation
    @Override
    public String getDeleteStoredProcedure()
    {
        return "{call m_inactivate_platform_by_id(?)}";
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObject(PreparedStatement stmt, Platform domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    public List<Platform> insertPlatform(List<Platform> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    public List<Platform> selectPlatform(List<Platform> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    public void updatePlatform(List<Platform> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }

    public void deletePlatform(List<Platform> prototypes) throws DAOException
    {
        PersistManager.delete(this, prototypes);
    }
}