package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.domain.ApplicationPlatformAttribute;

public interface ApplicationPlatformAttributeDAO
{
    List<ApplicationPlatformAttribute> insertApplicationPlatformAttribute(List<ApplicationPlatformAttribute> newVersions)
        throws DAOException;

    List<ApplicationPlatformAttribute> insertApplicationPlatformAttribute(
            List<ApplicationPlatformAttribute> newVersions, Connection conn) throws DAOException;

    List<ApplicationPlatformAttribute> selectApplicationPlatformAttribute(List<ApplicationPlatformAttribute> prototypes)
        throws DAOException;

    List<ApplicationPlatformAttribute> selectApplicationPlatformAttribute(
            List<ApplicationPlatformAttribute> prototypes, Connection conn, EnumSet<Select.Option> options)
        throws DAOException;

    void updateApplicationPlatformAttribute(List<ApplicationPlatformAttribute> newVersions) throws DAOException;

    void updateApplicationPlatformAttribute(List<ApplicationPlatformAttribute> newVersions, Connection conn)
        throws DAOException;
}
