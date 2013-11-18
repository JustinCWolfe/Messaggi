package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Delete;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.PersistManager.Update;
import com.messaggi.persistence.dao.UserDAO;
import com.messaggi.persistence.domain.User;

public class PostgreSQLUserDAO extends PostgreSQLBaseDAO<User> implements UserDAO, Insert<User>, Select<User>,
        Update<User>, Delete<User>
{
    private SelectBy selectBy;

    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_user(?,?,?,?,?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, User domainObject)
        throws SQLException
    {
        stmt.setString(1, domainObject.getName());
        stmt.setString(2, domainObject.getEmail());
        stmt.setString(3, domainObject.getPhone());
        stmt.setString(4, domainObject.getPassword());
        String locale = (domainObject.getLocale() != null) ? domainObject.getLocale().toLanguageTag() : null;
        stmt.setString(5, locale);
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, User domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setEmail(rs.getString("email"));
        domainObject.setPhone(rs.getString("phone"));
        domainObject.setPhoneParsed(rs.getString("phone_parsed"));
        domainObject.setPassword(rs.getString("password"));
        domainObject.setLocale(Locale.forLanguageTag(rs.getString("locale")));
        domainObject.setActive(rs.getBoolean("active"));
    }

    private static EnumSet<SelectBy> computeSelectBySetForPrototypes(List<User> prototypes)
    {
        EnumSet<SelectBy> selectBySet = EnumSet.noneOf(SelectBy.class);
        for (User prototype : prototypes) {
            if (prototype.getId() != null) {
                selectBySet.add(SelectBy.ID);
            }
            if (prototype.getEmail() != null) {
                selectBySet.add(SelectBy.EMAIL);
            }
        }
        return selectBySet;
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<User> prototypes) throws DAOException
    {
        EnumSet<SelectBy> selectBySet = computeSelectBySetForPrototypes(prototypes);
        String storedProcedure = null;
        if (selectBySet.size() > 1) {
            throw new DAOException(DAOException.ErrorCode.INVALID_CRITERIA,
                    UserDAO.Messages.BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        } else if (selectBySet.contains(SelectBy.EMAIL)) {
            selectBy = SelectBy.EMAIL;
            storedProcedure = "{call m_get_user_by_email(?)}";
        } else if (selectBySet.contains(SelectBy.ID)) {
            selectBy = SelectBy.ID;
            storedProcedure = "{call m_get_user_by_id(?)}";
        } else {
            throw new DAOException(DAOException.ErrorCode.INVALID_CRITERIA,
                    UserDAO.Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        }
        return storedProcedure;
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, User domainObject)
        throws SQLException
    {
        if (selectBy == SelectBy.EMAIL) {
            stmt.setString(1, domainObject.getEmail());
        } else {
            stmt.setLong(1, domainObject.getId());
        }
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, User domainObject) throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setEmail(rs.getString("email"));
        domainObject.setPhone(rs.getString("phone"));
        domainObject.setPhoneParsed(rs.getString("phone_parsed"));
        domainObject.setPassword(rs.getString("password"));
        domainObject.setLocale(Locale.forLanguageTag(rs.getString("locale")));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return "{call m_update_user(?,?,?,?,?,?,?)}";
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, User domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setString(2, domainObject.getName());
        stmt.setString(3, domainObject.getEmail());
        stmt.setString(4, domainObject.getPhone());
        stmt.setString(5, domainObject.getPassword());
        String locale = (domainObject.getLocale() != null) ? domainObject.getLocale().toLanguageTag() : null;
        stmt.setString(6, locale);
        stmt.setBoolean(7, domainObject.getActive());
    }

    // Delete implementation
    @Override
    public String getDeleteStoredProcedure()
    {
        return "{call m_inactivate_user_by_id(?)}";
    }

    @Override
    public void beforeDeleteInitializeStatementFromDomainObject(PreparedStatement stmt, User domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
    }

    @Override
    public List<User> insertUser(List<User> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<User> insertUser(List<User> newVersions, Connection conn) throws DAOException
    {
        return PersistManager.insert(this, newVersions, conn);
    }

    @Override
    public List<User> selectUser(List<User> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public List<User> selectUser(List<User> prototypes, Connection conn) throws DAOException
    {
        return PersistManager.select(this, prototypes, conn);
    }

    @Override
    public void updateUser(List<User> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }

    @Override
    public void updateUser(List<User> newVersions, Connection conn) throws DAOException
    {
        PersistManager.update(this, newVersions, conn);
    }

    @Override
    public void deleteUser(List<User> prototypes) throws DAOException
    {
        PersistManager.delete(this, prototypes);
    }

    @Override
    public void deleteUser(List<User> prototypes, Connection conn) throws DAOException
    {
        PersistManager.delete(this, prototypes, conn);
    }
}
