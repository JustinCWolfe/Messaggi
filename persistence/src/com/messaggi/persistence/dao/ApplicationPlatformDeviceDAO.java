package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.ApplicationPlatformDevice;

public interface ApplicationPlatformDeviceDAO
{
    List<ApplicationPlatformDevice> insertApplicationPlatformDevice(List<ApplicationPlatformDevice> newVersions)
        throws DAOException;

    List<ApplicationPlatformDevice> insertApplicationPlatformDevice(List<ApplicationPlatformDevice> newVersions,
            Connection conn) throws DAOException;

    List<ApplicationPlatformDevice> selectApplicationPlatformDevice(List<ApplicationPlatformDevice> prototypes)
        throws DAOException;

    List<ApplicationPlatformDevice> selectApplicationPlatformDevice(List<ApplicationPlatformDevice> prototypes,
            Connection conn) throws DAOException;
}
