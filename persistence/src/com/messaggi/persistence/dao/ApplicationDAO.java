package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.Application;

public interface ApplicationDAO
{
    List<Application> insertApplication(List<Application> newVersions) throws DAOException;

    List<Application> insertApplication(List<Application> newVersions, Connection conn) throws DAOException;

    List<Application> selectApplication(List<Application> prototypes) throws DAOException;

    List<Application> selectApplication(List<Application> prototypes, Connection conn) throws DAOException;

    void updateApplication(List<Application> newVersions) throws DAOException;

    void updateApplication(List<Application> newVersions, Connection conn) throws DAOException;

    void deleteApplication(List<Application> prototypes) throws DAOException;

    void deleteApplication(List<Application> prototypes, Connection conn) throws DAOException;
}

