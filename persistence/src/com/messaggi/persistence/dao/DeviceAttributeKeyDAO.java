package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.domain.DeviceAttributeKey;

public interface DeviceAttributeKeyDAO
{
    List<DeviceAttributeKey> selectDeviceAttributeKey(List<DeviceAttributeKey> prototypes) throws DAOException;

    List<DeviceAttributeKey> selectDeviceAttributeKey(List<DeviceAttributeKey> prototypes, Connection conn,
            EnumSet<Select.Option> options) throws DAOException;
}
