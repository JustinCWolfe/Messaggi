package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.domain.DeviceAttribute;

public interface DeviceAttributeDAO
{
    List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException;

    List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions, Connection conn) throws DAOException;

    List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes) throws DAOException;

    List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes, Connection conn,
            EnumSet<Select.Option> options) throws DAOException;

    void updateDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException;

    void updateDeviceAttribute(List<DeviceAttribute> newVersions, Connection conn) throws DAOException;
}

