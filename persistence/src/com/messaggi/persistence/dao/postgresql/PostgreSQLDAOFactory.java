package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.messaggi.persistence.dao.ApplicationDAO;
import com.messaggi.persistence.dao.ApplicationPlatformAttributeDAO;
import com.messaggi.persistence.dao.ApplicationPlatformDAO;
import com.messaggi.persistence.dao.ApplicationPlatformMsgLogDAO;
import com.messaggi.persistence.dao.DAOFactory;
import com.messaggi.persistence.dao.DeviceAttributeDAO;
import com.messaggi.persistence.dao.DeviceDAO;
import com.messaggi.persistence.dao.PlatformDAO;
import com.messaggi.persistence.dao.UserDAO;

public class PostgreSQLDAOFactory extends DAOFactory
{
    public static final String DB_DRIVER = "org.postgresql.Driver";

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/Messaggi";

    static final String DB_USER = "jcw_dev";

    static final String DB_PASSWORD = "jwolfema2226";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver?" + "Include in your library path!");
            e.printStackTrace();
        }
    }

    public static Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public ApplicationDAO getApplicationDAO()
    {
        return new PostgreSQLApplicationDAO();
    }

    @Override
    public ApplicationPlatformDAO getApplicationPlatformDAO()
    {
        return new PostgreSQLApplicationPlatformDAO();
    }

    @Override
    public ApplicationPlatformAttributeDAO getApplicationPlatformAttributeDAO()
    {
        return new PostgreSQLApplicationPlatformAttributeDAO();
    }

    @Override
    public ApplicationPlatformMsgLogDAO getApplicationPlatformMsgLogDAO()
    {
        return new PostgreSQLApplicationPlatformMsgLogDAO();
    }

    @Override
    public DeviceDAO getDeviceDAO()
    {
        return new PostgreSQLDeviceDAO();
    }

    @Override
    public DeviceAttributeDAO getDeviceAttributeDAO()
    {
        return new PostgreSQLDeviceAttributeDAO();
    }

    @Override
    public PlatformDAO getPlatformDAO()
    {
        return new PostgreSQLPlatformDAO();
    }

    @Override
    public UserDAO getUserDAO()
    {
        return new PostgreSQLUserDAO();
    }
}
