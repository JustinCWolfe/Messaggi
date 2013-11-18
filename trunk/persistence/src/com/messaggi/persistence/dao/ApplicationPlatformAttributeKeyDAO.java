package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.ApplicationPlatformAttributeKey;

public interface ApplicationPlatformAttributeKeyDAO
{
    List<ApplicationPlatformAttributeKey> selectApplicationPlatformAttributeKey(
            List<ApplicationPlatformAttributeKey> prototypes) throws DAOException;

    List<ApplicationPlatformAttributeKey> selectApplicationPlatformAttributeKey(
            List<ApplicationPlatformAttributeKey> prototypes, Connection conn) throws DAOException;
}

