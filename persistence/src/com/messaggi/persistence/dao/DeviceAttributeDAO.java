package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.List;

import com.messaggi.persistence.domain.DeviceAttribute;

public interface DeviceAttributeDAO
{
    List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException;

    List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions, Connection conn) throws DAOException;

    List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes) throws DAOException;

    List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes, Connection conn) throws DAOException;

    void updateDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException;

    void updateDeviceAttribute(List<DeviceAttribute> newVersions, Connection conn) throws DAOException;
}

