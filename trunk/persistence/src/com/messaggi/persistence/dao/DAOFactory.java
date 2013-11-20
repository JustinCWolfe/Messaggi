package com.messaggi.persistence.dao;

import com.messaggi.persistence.dao.postgresql.PostgreSQLDAOFactory;

public abstract class DAOFactory
{
    public enum Factory {
        POSTGRESQL
    }

    public abstract ApplicationDAO getApplicationDAO();

    public abstract ApplicationPlatformDAO getApplicationPlatformDAO();

    public abstract ApplicationPlatformAttributeDAO getApplicationPlatformAttributeDAO();

    public abstract ApplicationPlatformMsgLogDAO getApplicationPlatformMsgLogDAO();

    public abstract DeviceDAO getDeviceDAO();

    public abstract DeviceAttributeDAO getDeviceAttributeDAO();

    public abstract PlatformDAO getPlatformDAO();

    public abstract UserDAO getUserDAO();

    public static DAOFactory getDAOFactory(Factory factoryType)
    {
        switch (factoryType) {
            case POSTGRESQL:
                return new PostgreSQLDAOFactory();
            default:
                return null;
        }
    }
}

