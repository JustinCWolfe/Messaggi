package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;

import com.messaggi.persistence.dao.DAOFactory;
import com.messaggi.persistence.dao.PersistManager.Persist;

public abstract class PostgreSQLBaseDAO<T> implements Persist<T>
{
    protected static final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.Factory.POSTGRESQL);

    @Override
    public Connection createConnection() throws SQLException
    {
        return PostgreSQLDAOFactory.createConnection();
    }
}

