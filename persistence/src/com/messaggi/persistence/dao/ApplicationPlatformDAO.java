package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.ApplicationPlatform;

public interface ApplicationPlatformDAO
{
    List<ApplicationPlatform> insertApplicationPlatform(List<ApplicationPlatform> newVersions) throws DAOException;

    List<ApplicationPlatform> insertApplicationPlatform(List<ApplicationPlatform> newVersions, Connection conn)
        throws DAOException;

    List<ApplicationPlatform> selectApplicationPlatform(List<ApplicationPlatform> prototypes) throws DAOException;

    List<ApplicationPlatform> selectApplicationPlatform(List<ApplicationPlatform> prototypes, Connection conn)
        throws DAOException;
}
