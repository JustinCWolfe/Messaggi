package com.messaggi.web.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.messaggi.dao.PersistManager;
import com.messaggi.dao.PersistManager.Delete;
import com.messaggi.dao.PersistManager.Insert;
import com.messaggi.dao.PersistManager.Update;
import com.messaggi.web.domain.Application;

public class ApplicationDAO implements Insert<Application>, Update<Application>, Delete<Application>
{
    private static final String SAVE_STORED_PROCEDURE = "{call m_save_application(?)}";

    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return SAVE_STORED_PROCEDURE;
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        Connection conn = stmt.getConnection();
        for (Application domainObject : domainObjects) {
            //conn.createArrayOf()
            //stmt.setArray(1, );
            //stmt.setString(1, domainObject.getName());
        }
    }

    @Override
    public void afterInsertInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
        throws SQLException
    {
        for (Application domainObject : domainObjects) {
            //domainObject.setId(rs.getLong("id"));
            //domainObject.setName(rs.getString("name"));
            //domainObject.setActive(rs.getBoolean("active"));
        }
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return SAVE_STORED_PROCEDURE;
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        for (Application domainObject : domainObjects) {
            //stmt.setLong(1, domainObject.getId());
            //stmt.setString(2, domainObject.getName());
            //stmt.setBoolean(3, domainObject.getActive());
        }
    }

    @Override
    public void afterUpdateInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
        throws SQLException
    {
    }

    // Delete implementation
    @Override
    public String getDeleteStoredProcedure()
    {
        return SAVE_STORED_PROCEDURE;
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        for (Application domainObject : domainObjects) {
            //stmt.setLong(1, domainObject.getId());
        }
    }

    @Override
    public void afterDeleteInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
        throws SQLException
    {
    }

    public List<Application> insertApplication(List<Application> newVersions) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        return PersistManager.insert(this, newVersions);
    }

    public void updateApplication(List<Application> newVersions) throws NamingException, SQLException
    {
        PersistManager.update(this, newVersions);
    }

    public void deleteApplication(List<Application> prototypes) throws NamingException, SQLException
    {
        PersistManager.delete(this, prototypes);
    }
}
