package com.messaggi.persistence.dao;

import java.util.List;

import com.messaggi.persistence.domain.User;

public interface UserDAO
{
    List<User> insertUser(List<User> newVersions) throws DAOException;

    List<User> updateUser(List<User> newVersions) throws DAOException;

    List<User> selectUser(List<User> prototypes) throws DAOException;
}
