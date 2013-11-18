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

        public static final String NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Select protoypes must include either email or id.";
    }

    List<User> insertUser(List<User> newVersions) throws DAOException;

    List<User> insertUser(List<User> newVersions, Connection conn) throws DAOException;

    List<User> selectUser(List<User> prototypes) throws DAOException;

    List<User> selectUser(List<User> prototypes, Connection conn) throws DAOException;

    void updateUser(List<User> newVersions) throws DAOException;

    void updateUser(List<User> newVersions, Connection conn) throws DAOException;

    void deleteUser(List<User> prototypes) throws DAOException;

    void deleteUser(List<User> prototypes, Connection conn) throws DAOException;
}
