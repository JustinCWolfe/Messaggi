package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.messaggi.persistence.dao.ApplicationDAO;
import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Delete;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.PersistManager.Update;
import com.messaggi.persistence.domain.Application;

public class PostgreSQLApplicationDAO implements ApplicationDAO, Insert<Application>, Select<Application>,
        Update<Application>, Delete<Application>
{
    // Persist implementation
    @Override
    public Connection createConnection() throws SQLException
    {
        return PostgreSQLDAOFactory.createConnection();
    }

    @Override
    public String getDomainObjectIdentifier(Application domainObject)
    {
        return domainObject.getId().toString();
    }

    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_application(?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, Application domainObject)
        throws SQLException
    {
        stmt.setString(1, domainObject.getName());
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, Application domainObject)
        throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<Application> prototypes) throws DAOException
    {
        return "{call m_get_application_by_id(?)}";
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, Application domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, Application domainObject)
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
    public void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, Application domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setString(2, domainObject.getName());
        stmt.setBoolean(3, domainObject.getActive());
    }

    // Delete implementation
    @Override
    public String getDeleteStoredProcedure()
    {
        return "{call m_inactivate_application_by_id(?)}";
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObject(PreparedStatement stmt, Application domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public List<Application> insertApplication(List<Application> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<Application> insertApplication(List<Application> newVersions, Connection conn) throws DAOException
    {
        return PersistManager.insert(this, newVersions, conn);
    }

    @Override
    public List<Application> selectApplication(List<Application> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public List<Application> selectApplication(List<Application> prototypes, Connection conn) throws DAOException
    {
        return PersistManager.select(this, prototypes, conn);
    }

    @Override
    public void updateApplication(List<Application> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }

    @Override
    public void updateApplication(List<Application> newVersions, Connection conn) throws DAOException
    {
        PersistManager.update(this, newVersions, conn);
    }

    @Override
    public void deleteApplication(List<Application> prototypes) throws DAOException
    {
        PersistManager.delete(this, prototypes);
    }

    @Override
    public void deleteApplication(List<Application> prototypes, Connection conn) throws DAOException
    {
        PersistManager.delete(this, prototypes, conn);
    }
}