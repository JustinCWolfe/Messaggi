package com.messaggi.persistence.dao;

import java.util.List;

import com.messaggi.persistence.domain.ApplicationPlatformAttribute;

public interface ApplicationPlatformAttributeDAO
{
    List<ApplicationPlatformAttribute> insertApplicationPlatformAttribute(List<ApplicationPlatformAttribute> newVersions)
        throws DAOException;

    List<ApplicationPlatformAttribute> selectApplicationPlatformAttribute(List<ApplicationPlatformAttribute> prototypes)
        throws DAOException;

    void updateApplicationPlatformAttribute(List<ApplicationPlatformAttribute> newVersions) throws DAOException;
}
