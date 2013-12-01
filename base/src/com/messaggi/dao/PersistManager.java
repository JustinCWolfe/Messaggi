package com.messaggi.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class PersistManager
{
    public interface Insert<T>
    {
        String getInsertStoredProcedure();

        void beforeInsertInitializeStatementFromDomainObjects(PreparedStatement stmt, List<T> domainObjects)
            throws SQLException;

        void afterInsertInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws SQLException;
    }

    public interface Select<T>
    {
        String getSelectStoredProcedure(List<T> prototypes) throws SQLException;

        void beforeSelectInitializeStatementFromDomainObjects(PreparedStatement stmt, List<T> domainObjects)
            throws SQLException;

        void afterSelectInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws SQLException;
    }

    public interface Update<T>
    {
        String getUpdateStoredProcedure();

        void beforeUpdateInitializeStatementFromDomainObjects(PreparedStatement stmt, List<T> domainObjects)
            throws SQLException;

        void afterUpdateInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws SQLException;
    }

    public interface Delete<T>
    {
        String getDeleteStoredProcedure();

        void beforeDeleteInitializeStatementFromDomainObjects(PreparedStatement stmt, List<T> domainObjects)
            throws SQLException;

        void afterDeleteInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws SQLException;
    }

    private class Messages
    {
        public static final String DATASOURCE_NOT_FOUND_MESSAGE = "Datasource '%s' not found.";
    }
    
    private static final String MESSAGGI_DATABASE_JNDI_NAME = "java:/comp/env/jdbc/Messaggi";

    private static Connection getConnection() throws NamingException, SQLException
    {
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup(MESSAGGI_DATABASE_JNDI_NAME);
        if (ds == null) {
            throw new SQLException(String.format(Messages.DATASOURCE_NOT_FOUND_MESSAGE, ""));
        }
        return ds.getConnection();
    }

    public static <T> List<T> insert(Insert<T> persist, List<T> newVersions) throws NamingException, SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        try (Connection conn = getConnection()) {
            try (CallableStatement stmt = conn.prepareCall(persist.getInsertStoredProcedure());) {
                persist.beforeInsertInitializeStatementFromDomainObjects(stmt, newVersions);
                List<T> insertedVersions = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    persist.afterInsertInitializeDomainObjectsFromResultSet(rs, insertedVersions);
                }
                return insertedVersions;
            }
        }
    }

    public static <T> List<T> select(Select<T> persist, List<T> prototypes) throws NamingException, SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        try (Connection conn = getConnection();) {
            try (CallableStatement stmt = conn.prepareCall(persist.getSelectStoredProcedure(prototypes));) {
                persist.beforeSelectInitializeStatementFromDomainObjects(stmt, prototypes);
                List<T> selectedDomainObjects = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    persist.afterSelectInitializeDomainObjectsFromResultSet(rs, selectedDomainObjects);
                }
                return selectedDomainObjects;
            }
        }
    }

    public static <T> List<T> update(Update<T> persist, List<T> newVersions) throws NamingException, SQLException
    {
        try (Connection conn = getConnection();) {
            try (CallableStatement stmt = conn.prepareCall(persist.getUpdateStoredProcedure());) {
                persist.beforeUpdateInitializeStatementFromDomainObjects(stmt, newVersions);
                List<T> updatedDomainObjects = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    persist.afterUpdateInitializeDomainObjectsFromResultSet(rs, updatedDomainObjects);
                }
                return updatedDomainObjects;
            }
        }
    }

    /**
     * Delete is simply an update marking the user's active flag to false.
     * 
     * @param persist
     * @param prototypes
     * @throws SQLException
     */
    public static <T> List<T> delete(Delete<T> persist, List<T> prototypes) throws NamingException, SQLException
    {
        try (Connection conn = getConnection();) {
            try (CallableStatement stmt = conn.prepareCall(persist.getDeleteStoredProcedure());) {
                persist.beforeDeleteInitializeStatementFromDomainObjects(stmt, prototypes);
                List<T> updatedDomainObjects = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    persist.afterDeleteInitializeDomainObjectsFromResultSet(rs, updatedDomainObjects);
                }
                return updatedDomainObjects;
            }
        }
    }
}
