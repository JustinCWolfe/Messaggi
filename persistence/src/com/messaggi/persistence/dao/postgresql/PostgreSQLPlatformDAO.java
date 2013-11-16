package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Delete;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.PersistManager.Update;
import com.messaggi.persistence.dao.PlatformDAO;
import com.messaggi.persistence.domain.Platform;

public class PostgreSQLPlatformDAO implements PlatformDAO, Insert<Platform>, Select<Platform>, Update<Platform>,
        Delete<Platform>
{
    // Persist implementation
    @Override
    public Connection createConnection() throws SQLException
    {
        return PostgreSQLDAOFactory.createConnection();
    }

    @Override
    public String getDomainObjectIdentifier(Platform domainObject)
    {
        return domainObject.getId().toString();
    }

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
        stmt.setString(2, domainObject.getServiceName());
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, Platform domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
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
        domainObject.setServiceName(rs.getString("service_name"));
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
        stmt.setString(3, domainObject.getServiceName());
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

    @Override
    public List<Platform> insertPlatform(List<Platform> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<Platform> insertPlatform(List<Platform> newVersions, Connection conn) throws DAOException
    {
        return PersistManager.insert(this, newVersions, conn);
    }

    @Override
    public List<Platform> selectPlatform(List<Platform> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public List<Platform> selectPlatform(List<Platform> prototypes, Connection conn) throws DAOException
    {
        return PersistManager.select(this, prototypes, conn);
    }

    @Override
    public void updatePlatform(List<Platform> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }

    @Override
    public void updatePlatform(List<Platform> newVersions, Connection conn) throws DAOException
    {
        PersistManager.update(this, newVersions, conn);
    }

    @Override
    public void deletePlatform(List<Platform> prototypes) throws DAOException
    {
        PersistManager.delete(this, prototypes);
    }

    @Override
    public void deletePlatform(List<Platform> prototypes, Connection conn) throws DAOException
    {
        PersistManager.delete(this, prototypes, conn);
    }
}