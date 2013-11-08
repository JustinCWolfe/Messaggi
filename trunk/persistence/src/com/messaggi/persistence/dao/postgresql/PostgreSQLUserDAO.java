package com.messaggi.persistence.dao.postgresql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.DAOException.ErrorCode;
import com.messaggi.persistence.dao.DAOHelper;
import com.messaggi.persistence.dao.UserDAO;
import com.messaggi.persistence.domain.User;

public class PostgreSQLUserDAO implements UserDAO
{
    private class StoredProcedures
    {
        private static final String INSERT_STORED_PROCEDURE = "{call m_create_user(?,?,?,?,?)}";

        private static final String SELECT_BY_EMAIL_STORED_PROCEDURE = "{call m_get_user_by_email(?)}";

        private static final String SELECT_BY_ID_STORED_PROCEDURE = "{call m_get_user_by_id(?)}";

        private static final String UPDATE_STORED_PROCEDURE = "{call m_update_user(?,?,?,?,?,?,?)}";

        private static final String DELETE_STORED_PROCEDURE = "{call m_inactivate_user_by_id(?)}";
    }

    private static Logger log = Logger.getLogger(PostgreSQLUserDAO.class);

    @Override
    public List<User> insertUser(List<User> newVersions) throws DAOException
    {
        try (Connection conn = PostgreSQLDAOFactory.createConnection();) {
            try {
                conn.setAutoCommit(false);
                List<User> users = insertUser(conn, newVersions);
                conn.commit();
                return users;
            } catch (DAOException e) {
	            conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.FAIL_TO_INSERT, e.getMessage());
        }
    }

    @Override
    public List<User> insertUser(Connection conn, List<User> newVersions) throws DAOException
    {
        try (CallableStatement stmt = conn.prepareCall(StoredProcedures.INSERT_STORED_PROCEDURE);) {
            List<User> users = new ArrayList<>();
            for (User newVersion : newVersions) {
                stmt.setString(1, newVersion.getName());
                stmt.setString(2, newVersion.getEmail());
                stmt.setString(3, newVersion.getPhone());
                stmt.setString(4, newVersion.getPassword());
                String locale = (newVersion.getLocale() != null) ? newVersion.getLocale().toLanguageTag() : null;
                stmt.setString(5, locale);
                try (ResultSet rs = stmt.executeQuery();) {
                    while (rs.next()) {
		                User user = DAOHelper.clonePrototype(newVersion);
                        user.setId(rs.getLong("id"));
                        user.setActive(rs.getBoolean("active"));
                        user.setPhoneParsed(rs.getString("phone_parsed"));
                        users.add(user);
                    }
                }
            }
            return users;
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.FAIL_TO_INSERT, e.getMessage());
        }
    }

    private SelectBy getSelectByForPrototypes(List<User> prototypes) throws DAOException
    {
        EnumSet<SelectBy> selectBySet = EnumSet.noneOf(SelectBy.class);
        for (User user : prototypes) {
            if (user.getId() != null) {
                selectBySet.add(SelectBy.ID);
            }
            if (user.getEmail() != null) {
                selectBySet.add(SelectBy.EMAIL);
            }
        }
        if (selectBySet.size() > 1) {
            throw new DAOException(ErrorCode.INVALID_CRITERIA,
                    UserDAO.Messages.BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        } else if (selectBySet.contains(SelectBy.EMAIL)) {
            return SelectBy.EMAIL;
        } else if (selectBySet.contains(SelectBy.ID)) {
            return SelectBy.ID;
        } else {
            throw new DAOException(ErrorCode.INVALID_CRITERIA,
                    UserDAO.Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        }
    }
    private String getSelectStoredProcedure(SelectBy selectBy) throws DAOException
    {
        switch (selectBy) {
            case EMAIL:
                return StoredProcedures.SELECT_BY_EMAIL_STORED_PROCEDURE;
            case ID:
                return StoredProcedures.SELECT_BY_ID_STORED_PROCEDURE;
            default:
                throw new DAOException(ErrorCode.INVALID_CRITERIA,
                        UserDAO.Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        }
    }

    @Override
    public List<User> selectUser(List<User> prototypes) throws DAOException
    {
        try (Connection conn = PostgreSQLDAOFactory.createConnection();) {
            return selectUser(conn, prototypes);
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.SQL_ERROR, e.getMessage());
        }
    }

    @Override
    public List<User> selectUser(Connection conn, List<User> prototypes) throws DAOException
    {
        try {
            SelectBy selectBy = getSelectByForPrototypes(prototypes);
            String selectStoredProcedure = getSelectStoredProcedure(selectBy);
            try (CallableStatement stmt = conn.prepareCall(selectStoredProcedure);) {
                List<User> users = new ArrayList<>();
                for (User prototype : prototypes) {
                    if (selectBy == SelectBy.EMAIL) {
                        stmt.setString(1, prototype.getEmail());
                    } else {
                        stmt.setLong(1, prototype.getId());
                    }
                    try (ResultSet rs = stmt.executeQuery();) {
                        while (rs.next()) {
                            User user = DAOHelper.clonePrototype(prototype);
                            user.setId(rs.getLong("id"));
                            user.setName(rs.getString("name"));
                            user.setEmail(rs.getString("email"));
                            user.setPhone(rs.getString("phone"));
                            user.setPhoneParsed(rs.getString("phone_parsed"));
                            user.setLocale(Locale.forLanguageTag(rs.getString("locale")));
                            user.setActive(rs.getBoolean("active"));
                            users.add(user);
                        }
                    }
                }
                return users;
            } catch (SQLException e) {
                log.error(e);
                throw new DAOException(ErrorCode.SQL_ERROR, e.getMessage());
            }
        } catch (DAOException e) {
            log.error(e);
            throw e;
        }
    }

    @Override
    public void updateUser(List<User> newVersions) throws DAOException
    {
        try (Connection conn = PostgreSQLDAOFactory.createConnection();) {
            try {
                conn.setAutoCommit(false);
                updateUser(conn, newVersions);
                conn.commit();
            } catch (DAOException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    @Override
    public void updateUser(Connection conn, List<User> newVersions) throws DAOException
    {
        try (CallableStatement stmt = conn.prepareCall(StoredProcedures.UPDATE_STORED_PROCEDURE);) {
            for (User newVersion : newVersions) {
                stmt.setLong(1, newVersion.getId());
                stmt.setString(2, newVersion.getName());
                stmt.setString(3, newVersion.getEmail());
                stmt.setString(4, newVersion.getPhone());
                stmt.setString(5, newVersion.getPassword());
                String locale = (newVersion.getLocale() != null) ? newVersion.getLocale().toLanguageTag() : null;
                stmt.setString(6, locale);
                stmt.setBoolean(7, newVersion.getActive());
                stmt.addBatch();
            }
            int[] updateCounts = stmt.executeBatch();
            int updateFailedCount = 0;
            for (int updateCountIndex = 0; updateCountIndex < updateCounts.length; updateCountIndex++) {
                if (updateCounts[updateCountIndex] == Statement.EXECUTE_FAILED) {
                    log.error(String.format(UserDAO.Messages.UPDATE_FAILED_FOR_ID_MESSAGE,
                            newVersions.get(updateCountIndex).getId()));
                    updateFailedCount++;
                }
            }
            if (updateFailedCount > 0) {
                throw new DAOException(ErrorCode.UPDATE_FAILED, String.format(UserDAO.Messages.UPDATE_FAILED_MESSAGE,
                        updateFailedCount));
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteUser(List<User> prototypes) throws DAOException
    {
        // Delete is simply an update marking the user's active flag to false.
        try (Connection conn = PostgreSQLDAOFactory.createConnection();) {
            try {
                conn.setAutoCommit(false);
                deleteUser(conn, prototypes);
                conn.commit();
            } catch (DAOException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteUser(Connection conn, List<User> prototypes) throws DAOException
    {
        try (CallableStatement stmt = conn.prepareCall(StoredProcedures.DELETE_STORED_PROCEDURE);) {
            for (User newVersion : prototypes) {
                stmt.setLong(1, newVersion.getId());
                stmt.addBatch();
            }
            int[] updateCounts = stmt.executeBatch();
            int updateFailedCount = 0;
            for (int updateCountIndex = 0; updateCountIndex < updateCounts.length; updateCountIndex++) {
                if (updateCounts[updateCountIndex] == Statement.EXECUTE_FAILED) {
                    log.error(String.format(UserDAO.Messages.UPDATE_FAILED_FOR_ID_MESSAGE,
                            prototypes.get(updateCountIndex).getId()));
                    updateFailedCount++;
                }
            }
            if (updateFailedCount > 0) {
                throw new DAOException(ErrorCode.UPDATE_FAILED, String.format(UserDAO.Messages.UPDATE_FAILED_MESSAGE,
                        updateFailedCount));
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }
}

