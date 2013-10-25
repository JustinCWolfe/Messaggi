package com.messaggi.persistence.dao;

public abstract class DAOFactory
{
    public enum Factory {
        PostgreSQL
    }

    public abstract ApplicationDAO getApplicationDAO();

    public abstract ApplicationPlatformDAO getApplicationPlatformDAO();

    public abstract ApplicationPlatformAttributeDAO getApplicationPlatformAttributeDAO();

    public abstract ApplicationPlatformKeyDAO getApplicationPlatformKeyDAO();

    public abstract PlatformDAO getPlatformDAO();

    public abstract UserDAO getUserDAO();

    public static DAOFactory getDAOFactory(Factory factoryType)
    {
        switch (factoryType) {
            case PostgreSQL:
                return null;
            default:
                return null;
        }
    }
}

