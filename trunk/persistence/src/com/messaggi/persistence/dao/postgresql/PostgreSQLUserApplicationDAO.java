package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.UserApplicationDAO;
import com.messaggi.persistence.domain.UserApplication;

public class PostgreSQLUserApplicationDAO extends PostgreSQLBaseDAO<UserApplication> implements UserApplicationDAO,
        Insert<UserApplication>, Select<UserApplication>
{
    private SelectBy selectBy;

    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_user_application(?,?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, UserApplication domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getUser().getId());
        stmt.setLong(2, domainObject.getApplication().getId());
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, UserApplication domainObject)
        throws SQLException
    {
        domainObject.getUser().setId(rs.getLong("user_id"));
        domainObject.getApplication().setId(rs.getLong("application_id"));
    }

    private static EnumSet<SelectBy> computeSelectBySetForPrototypes(List<UserApplication> prototypes)
    {
        EnumSet<SelectBy> selectBySet = EnumSet.noneOf(SelectBy.class);
        for (UserApplication prototype : prototypes) {
            if (prototype.getApplication() != null && prototype.getApplication().getId() != null) {
                selectBySet.add(SelectBy.APPLICATION_ID);
            }
            if (prototype.getUser() != null && prototype.getUser().getId() != null) {
                selectBySet.add(SelectBy.USER_ID);
            }
        }
        return selectBySet;
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<UserApplication> prototypes) throws DAOException
    {
        EnumSet<SelectBy> selectBySet = computeSelectBySetForPrototypes(prototypes);
        String storedProcedure = null;
        if (selectBySet.size() > 1) {
            throw new DAOException(DAOException.ErrorCode.INVALID_CRITERIA,
                    UserApplicationDAO.Messages.BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        } else if (selectBySet.contains(SelectBy.APPLICATION_ID)) {
            selectBy = SelectBy.APPLICATION_ID;
            storedProcedure = "{call m_get_user_application_by_application_id(?)}";
        } else if (selectBySet.contains(SelectBy.USER_ID)) {
            selectBy = SelectBy.USER_ID;
            storedProcedure = "{call m_get_user_application_by_user_id(?)}";
        } else {
            throw new DAOException(DAOException.ErrorCode.INVALID_CRITERIA,
                    UserApplicationDAO.Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        }
        return storedProcedure;
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, UserApplication domainObject)
        throws SQLException
    {
        if (selectBy == SelectBy.APPLICATION_ID) {
            stmt.setLong(1, domainObject.getApplication().getId());
        } else {
            stmt.setLong(1, domainObject.getUser().getId());
        }
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, UserApplication domainObject)
        throws SQLException
    {
        domainObject.getUser().setId(rs.getLong("user_id"));
        domainObject.getApplication().setId(rs.getLong("application_id"));
    }

    @Override
    public List<UserApplication> insertUserApplication(List<UserApplication> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<UserApplication> insertUserApplication(List<UserApplication> newVersions, Connection conn)
        throws DAOException
    {
        return PersistManager.insert(this, newVersions, conn);
    }

    @Override
    public List<UserApplication> selectUserApplication(List<UserApplication> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public List<UserApplication> selectUserApplication(List<UserApplication> prototypes, Connection conn)
        throws DAOException
    {
        return PersistManager.select(this, prototypes, conn);
    }
}

