package com.messaggi.web.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.messaggi.dao.PersistManager;
import com.messaggi.dao.PersistManager.Delete;
import com.messaggi.dao.PersistManager.Insert;
import com.messaggi.dao.PersistManager.Select;
import com.messaggi.dao.PersistManager.Update;
import com.messaggi.web.domain.Application;

public class ApplicationDAO implements Insert<Application>, Select<Application>, Update<Application>, Delete<Application>
{
    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_application(?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        stmt.setString(1, domainObject.getName());
    }

    @Override
    public void afterInsertInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
        throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<Application> prototypes) throws SQLException
    {
        return "{call m_get_application_by_id(?)}";
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public void afterSelectInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
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
    public void beforeUpdateInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setString(2, domainObject.getName());
        stmt.setBoolean(3, domainObject.getActive());
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
        return "{call m_inactivate_application_by_id(?)}";
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObjects(PreparedStatement stmt, List<Application> domainObjects)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
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

    public List<Application> selectApplication(List<Application> prototypes) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        return PersistManager.select(this, prototypes);
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
