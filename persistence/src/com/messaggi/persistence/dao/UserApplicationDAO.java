package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.domain.UserApplication;

public interface UserApplicationDAO
{
    enum SelectBy {
        APPLICATION_ID, USER_ID
    };

    class Messages
    {
        public static final String BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Select prototypes can include application id or user id, not both.";

        public static final String NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Select protoypes must include either application id or user id.";
    }

    List<UserApplication> insertUserApplication(List<UserApplication> newVersions) throws DAOException;

    List<UserApplication> insertUserApplication(List<UserApplication> newVersions, Connection conn) throws DAOException;

    List<UserApplication> selectUserApplication(List<UserApplication> prototypes) throws DAOException;

    List<UserApplication> selectUserApplication(List<UserApplication> prototypes, Connection conn,
            EnumSet<Select.Option> options) throws DAOException;
}