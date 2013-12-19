package com.messaggi.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.beanutils.BeanUtils;

public class DAOHelper
{
    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433/Messaggi";

    private static final String DB_USER = "jcw_dev";

    private static final String DB_PASSWORD = "jwolfema2226";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your SqlServer JDBC Driver?" + "Include in your library path!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Object> T clonePrototype(T prototype) throws Exception
    {
        return (T) BeanUtils.cloneBean(prototype);
    }

    public static Connection createConnection() throws Exception
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

