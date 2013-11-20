package com.messaggi.persistence.dao.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.messaggi.persistence.dao.ApplicationPlatformAttributeDAO;
import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.PersistManager.Update;
import com.messaggi.persistence.domain.ApplicationPlatformAttribute;

public class PostgreSQLApplicationPlatformAttributeDAO implements ApplicationPlatformAttributeDAO,
        Insert<ApplicationPlatformAttribute>, Select<ApplicationPlatformAttribute>,
        Update<ApplicationPlatformAttribute>
{
    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_application_platform_attribute(?,?,?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt,
            ApplicationPlatformAttribute domainObject) throws SQLException
    {
        stmt.setLong(1, domainObject.getApplicationPlatform().getId());
        stmt.setString(2, domainObject.getKey());
        stmt.setString(3, domainObject.getValue());
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, ApplicationPlatformAttribute domainObject)
        throws SQLException
    {
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<ApplicationPlatformAttribute> prototypes) throws DAOException
    {
        return "{call m_get_application_by_id(?)}";
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt,
            ApplicationPlatformAttribute domainObject) throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, ApplicationPlatformAttribute domainObject)
        throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return "{call m_update_application(?,?,?)}";
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt,
            ApplicationPlatformAttribute domainObject) throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setString(2, domainObject.getName());
        stmt.setBoolean(3, domainObject.getActive());
    }

    @Override
    public List<ApplicationPlatformAttribute> insertApplicationPlatformAttribute(
            List<ApplicationPlatformAttribute> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<ApplicationPlatformAttribute> selectApplicationPlatformAttribute(
            List<ApplicationPlatformAttribute> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public void updateApplicationPlatformAttribute(List<ApplicationPlatformAttribute> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }
}

