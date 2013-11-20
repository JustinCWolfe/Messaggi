package com.messaggi.persistence.dao;

import java.util.List;

import com.messaggi.persistence.domain.Platform;

public interface PlatformDAO
{
    List<Platform> insertPlatform(List<Platform> newVersions) throws DAOException;

    List<Platform> selectPlatform(List<Platform> prototypes) throws DAOException;

    void updatePlatform(List<Platform> newVersions) throws DAOException;

    void deletePlatform(List<Platform> prototypes) throws DAOException;
}

