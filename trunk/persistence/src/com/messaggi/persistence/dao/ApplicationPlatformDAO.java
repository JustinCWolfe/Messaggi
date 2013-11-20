package com.messaggi.persistence.dao;

import java.util.List;

import com.messaggi.persistence.domain.ApplicationPlatform;

public interface ApplicationPlatformDAO
{
    List<ApplicationPlatform> insertApplicationPlatform(List<ApplicationPlatform> newVersions) throws DAOException;

    List<ApplicationPlatform> selectApplicationPlatform(List<ApplicationPlatform> prototypes) throws DAOException;
}
