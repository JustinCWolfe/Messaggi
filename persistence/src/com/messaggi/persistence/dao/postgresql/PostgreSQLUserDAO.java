package com.messaggi.persistence.dao.postgresql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.DAOException.ErrorCode;
import com.messaggi.persistence.dao.UserDAO;
import com.messaggi.persistence.domain.User;

public class PostgreSQLUserDAO implements UserDAO
{
    private static Log log = LogFactory.getLog(PostgreSQLUserDAO.class);

    @Override
    public List<User> insertUser(List<User> newVersions) throws DAOException
    {
        String insertStoredProcedure = "{call m_create_user(?,?,?,?,?)}";

        List<User> users = new ArrayList<>();
        // Cannot use batch update processing here because we need to get a result
        // set back from the stored procedure with the newly inserted records.
        try (Connection conn = PostgreSQLDAOFactory.createConnection();
                CallableStatement stmt = conn.prepareCall(insertStoredProcedure);) {
            for (User newVersion : newVersions) {
                stmt.setString(1, newVersion.getName());
                stmt.setString(2, newVersion.getEmail());
                stmt.setString(3, newVersion.getPhone());
                stmt.setString(4, newVersion.getPassword());
                String locale = (newVersion.getLocale() != null) ? newVersion.getLocale().toLanguageTag() : null;
                stmt.setString(5, locale);

                User user = null;
                try {
                    user = (User) BeanUtils.cloneBean(newVersion);
                } catch (Exception e) {
                    log.error(e);
                    throw new DAOException(ErrorCode.CLONE_ERROR, e.getMessage());
                }

                try (ResultSet rs = stmt.executeQuery();) {
                    while (rs.next()) {
                        user.setId(UUID.fromString(rs.getString("id")));
                        user.setActive(rs.getBoolean("active"));
                        user.setPhoneParsed(rs.getInt("phone_parsed"));
                    }
                }
                users.add(user);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.FAIL_TO_INSERT, e.getMessage());
        }
        return users;
    }

    @Override
    public List<User> updateUser(List<User> newVersions) throws DAOException
    {
        return null;
    }

    @Override
    public List<User> selectUser(List<User> prototypes) throws DAOException
    {
        return null;
    }
}

