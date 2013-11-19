package com.messaggi.persistence.dao;

import java.sql.Connection;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.domain.Device;

public interface DeviceDAO
{
    List<Device> insertDevice(List<Device> newVersions) throws DAOException;

    List<Device> insertDevice(List<Device> newVersions, Connection conn) throws DAOException;

    List<Device> selectDevice(List<Device> prototypes) throws DAOException;

    List<Device> selectDevice(List<Device> prototypes, Connection conn, EnumSet<Select.Option> options)
        throws DAOException;

    void updateDevice(List<Device> newVersions) throws DAOException;

    void updateDevice(List<Device> newVersions, Connection conn) throws DAOException;

    void deleteDevice(List<Device> prototypes) throws DAOException;

    void deleteDevice(List<Device> prototypes, Connection conn) throws DAOException;
}

