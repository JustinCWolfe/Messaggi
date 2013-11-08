package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.User;

public interface UserDAO
{
    enum SelectBy {
        EMAIL, ID
    };

    class Messages
    {
        public static final String BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Select prototypes can include email or id, not both.";

        public static final String NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Select proptypes must include either email or id.";

        public static final String UPDATE_FAILED_FOR_ID_MESSAGE = "Update failed for id {0}.";

        public static final String UPDATE_FAILED_MESSAGE = "{0} updates failed.";
    }

    List<User> insertUser(List<User> newVersions) throws DAOException;

    List<User> insertUser(Connection conn, List<User> newVersions) throws DAOException;

    List<User> selectUser(List<User> prototypes) throws DAOException;

    List<User> selectUser(Connection conn, List<User> prototypes) throws DAOException;

    void updateUser(List<User> newVersions) throws DAOException;

    void updateUser(Connection conn, List<User> newVersions) throws DAOException;

    void deleteUser(List<User> prototypes) throws DAOException;

    void deleteUser(Connection conn, List<User> prototypes) throws DAOException;
}
