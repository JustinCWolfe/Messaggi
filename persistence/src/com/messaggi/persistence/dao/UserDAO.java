package com.messaggi.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.messaggi.persistence.domain.User;

public interface UserDAO
{
    List<User> insertUser(List<User> newVersions) throws SQLException;

    List<User> updateUser(List<User> newVersions) throws SQLException;;

    List<User> selectUser(List<User> prototypes) throws SQLException;;
}
