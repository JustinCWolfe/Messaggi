package com.messaggi.persistence.dao;

import java.util.List;

import com.messaggi.persistence.domain.DeviceAttribute;

public interface DeviceAttributeDAO
{
    enum SelectBy {
        ID, ID_AND_KEY
    };

    List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException;

    List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes) throws DAOException;

    void updateDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException;
}

