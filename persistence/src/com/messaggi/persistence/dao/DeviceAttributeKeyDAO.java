package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.DeviceAttributeKey;

public interface DeviceAttributeKeyDAO
{
    List<DeviceAttributeKey> selectDeviceAttributeKey(List<DeviceAttributeKey> prototypes) throws DAOException;

    List<DeviceAttributeKey> selectDeviceAttributeKey(List<DeviceAttributeKey> prototypes, Connection conn)
        throws DAOException;
}
