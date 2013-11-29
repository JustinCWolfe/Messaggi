package com.messaggi.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class PersistManager
{
    public interface Insert<T>
    {
        String getInsertStoredProcedure();

        void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;

        void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, T domainObject) throws SQLException;
    }

    public interface Select<T>
    {
        String getSelectStoredProcedure(List<T> prototypes) throws SQLException;

        void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;

        void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, T domainObject) throws SQLException;
    }

    public interface Update<T>
    {
        String getUpdateStoredProcedure();

        void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;
    }

    public interface Delete<T>
    {
        String getDeleteStoredProcedure();

        void beforeDeleteInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;
    }

    private class Messages
    {
        public static final String DATASOURCE_NOT_FOUND_MESSAGE = "Datasource '%s' not found.";

        public static final String UPDATE_FAILED_FOR_ID_MESSAGE = "Update failed for id %s.";

        public static final String UPDATE_FAILED_MESSAGE = "%s updates failed.";
    }
    
    private static Logger log = Logger.getLogger(PersistManager.class);
    
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
            try {
                conn.setAutoCommit(false);
                List<T> insertedVersions = insert(persist, newVersions, conn);
                conn.commit();
                return insertedVersions;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static <T> List<T> insert(Insert<T> persist, List<T> newVersions, Connection conn) throws SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        try (CallableStatement stmt = conn.prepareCall(persist.getInsertStoredProcedure());) {
            List<T> insertedVersions = new ArrayList<>();
            for (T newVersion : newVersions) {
                persist.beforeInsertInitializeStatementFromDomainObject(stmt, newVersion);
                try (ResultSet rs = stmt.executeQuery();) {
                    while (rs.next()) {
                        T insertedVersion = DAOHelper.clonePrototype(newVersion);
                        persist.afterInsertInitializeDomainObjectFromResultSet(rs, insertedVersion);
                        insertedVersions.add(insertedVersion);
                    }
                }
            }
            return insertedVersions;
        }
    }

    public static <T> List<T> select(Select<T> persist, List<T> prototypes) throws NamingException, SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        try (Connection conn = getConnection();) {
            conn.setAutoCommit(false);
            List<T> selectedVersions = select(persist, prototypes, conn);
            conn.commit();
            return selectedVersions;
        }
    }

    public static <T> List<T> select(Select<T> persist, List<T> prototypes, Connection conn) throws SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        try (CallableStatement stmt = conn.prepareCall(persist.getSelectStoredProcedure(prototypes));) {
            List<T> selectedDomainObjects = new ArrayList<>();
            for (T prototype : prototypes) {
                persist.beforeSelectInitializeStatementFromDomainObject(stmt, prototype);
                try (ResultSet rs = stmt.executeQuery();) {
                    while (rs.next()) {
                        T selectedDomainObject = DAOHelper.clonePrototype(prototype);
                        persist.afterSelectInitializeDomainObjectFromResultSet(rs, selectedDomainObject);
                        selectedDomainObjects.add(selectedDomainObject);
                    }
                }
            }
            return selectedDomainObjects;
        }
    }

    private static <T> void updateInternal(Statement stmt, List<T> prototypes) throws SQLException
    {
        int[] updateCounts = stmt.executeBatch();
        int updateFailedCount = 0;
        for (int updateCountIndex = 0; updateCountIndex < updateCounts.length; updateCountIndex++) {
            if (updateCounts[updateCountIndex] == Statement.EXECUTE_FAILED) {
                String objectIndentifier = prototypes.get(updateCountIndex).toString();
                log.error(String.format(Messages.UPDATE_FAILED_FOR_ID_MESSAGE, objectIndentifier));
                updateFailedCount++;
            }
        }
        if (updateFailedCount > 0) {
            throw new SQLException(String.format(Messages.UPDATE_FAILED_MESSAGE, updateFailedCount));
        }
    }

    public static <T> void update(Update<T> persist, List<T> newVersions) throws NamingException, SQLException
    {
        try (Connection conn = getConnection();) {
            try {
                conn.setAutoCommit(false);
                update(persist, newVersions, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static <T> void update(Update<T> persist, List<T> newVersions, Connection conn) throws SQLException
    {
        try (CallableStatement stmt = conn.prepareCall(persist.getUpdateStoredProcedure());) {
            for (T newVersion : newVersions) {
                persist.beforeUpdateInitializeStatementFromDomainObject(stmt, newVersion);
                stmt.addBatch();
            }
            updateInternal(stmt, newVersions);
        }
    }

    /**
     * Delete is simply an update marking the user's active flag to false.
     * 
     * @param persist
     * @param prototypes
     * @throws SQLException
     */
    public static <T> void delete(Delete<T> persist, List<T> prototypes) throws NamingException, SQLException
    {
        try (Connection conn = getConnection();) {
            try {
                conn.setAutoCommit(false);
                delete(persist, prototypes, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
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
    public static <T> void delete(Delete<T> persist, List<T> prototypes, Connection conn) throws SQLException
    {
        try (CallableStatement stmt = conn.prepareCall(persist.getDeleteStoredProcedure());) {
            for (T prototype : prototypes) {
                persist.beforeDeleteInitializeStatementFromDomainObject(stmt, prototype);
                stmt.addBatch();
            }
            updateInternal(stmt, prototypes);
        }
    }
}
