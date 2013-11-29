package com.messaggi.web.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import com.messaggi.dao.PersistManager;
import com.messaggi.dao.PersistManager.Delete;
import com.messaggi.dao.PersistManager.Insert;
import com.messaggi.dao.PersistManager.Select;
import com.messaggi.dao.PersistManager.Update;
import com.messaggi.web.domain.User;

public class UserDAO implements Insert<User>, Select<User>, Update<User>, Delete<User>
{
    private enum SelectBy {
        ID, EMAIL
    };

    private class Messages
    {
        private static final String BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Can select by id or email, not both.";

        private static final String NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Must specify id or email as select criteria.";
    }

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
    public String getSelectStoredProcedure(List<User> prototypes) throws SQLException
    {
        EnumSet<SelectBy> selectBySet = computeSelectBySetForPrototypes(prototypes);
        String storedProcedure = null;
        if (selectBySet.size() > 1) {
            throw new SQLException(Messages.BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        } else if (selectBySet.contains(SelectBy.EMAIL)) {
            selectBy = SelectBy.EMAIL;
            storedProcedure = "{call m_get_user_by_email(?)}";
        } else if (selectBySet.contains(SelectBy.ID)) {
            selectBy = SelectBy.ID;
            storedProcedure = "{call m_get_user_by_id(?)}";
        } else {
            throw new SQLException(Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
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

    public List<User> insertUser(List<User> newVersions) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        return PersistManager.insert(this, newVersions);
    }

    public List<User> selectUser(List<User> prototypes) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        return PersistManager.select(this, prototypes);
    }

    public void updateUser(List<User> newVersions) throws NamingException, SQLException
    {
        PersistManager.update(this, newVersions);
    }

    public void deleteUser(List<User> prototypes) throws NamingException, SQLException
    {
        PersistManager.delete(this, prototypes);
    }
}
