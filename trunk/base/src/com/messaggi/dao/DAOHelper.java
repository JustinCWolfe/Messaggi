package com.messaggi.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;

public class DAOHelper
{
    private static final String DB_DRIVER = "org.postgresql.Driver";

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Messaggi";

    private static final String DB_USER = "jcw_dev";

    private static final String DB_PASSWORD = "jwolfema2226";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver?" + "Include in your library path!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Object> T clonePrototype(T prototype) throws IllegalAccessException,
        InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        return (T) BeanUtils.cloneBean(prototype);
    }

    public static Connection createConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

