package com.messaggi.persistence.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class PersistManager
{
    public interface Persist<T>
    {
        Connection createConnection() throws SQLException;
    }

    public interface Insert<T> extends Persist<T>
    {
        String getInsertStoredProcedure();

        void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;

        void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, T domainObject) throws SQLException;
    }

    public interface Select<T> extends Persist<T>
    {
        public enum Option {
            RETRIEVE_DEPENDENT_OBJECTS
        }

        String getSelectStoredProcedure(List<T> prototypes) throws DAOException;

        void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;

        void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, T domainObject) throws SQLException;
    }

    public interface SelectWithDependencyLoad<T> extends Select<T>
    {
        String getDependentObjectLoadErrorMessage();
    }

    public interface Update<T> extends Persist<T>
    {
        String getUpdateStoredProcedure();

        void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;
    }

    public interface Delete<T> extends Persist<T>
    {
        String getDeleteStoredProcedure();

        void beforeDeleteInitializeStatementFromDomainObject(PreparedStatement stmt, T domainObject)
            throws SQLException;
    }

    private class Messages
    {
        public static final String UPDATE_FAILED_FOR_ID_MESSAGE = "Update failed for id %s.";

        public static final String UPDATE_FAILED_MESSAGE = "%s updates failed.";

        public static final String SELECT_DEPENDENT_OBJECT_FAILED_MESSAGE = "Dependent object select failed for: %s.";
    }

    private static Logger log = Logger.getLogger(PersistManager.class);

    public static <T> List<T> insert(Insert<T> persist, List<T> newVersions) throws DAOException
    {
        try (Connection conn = persist.createConnection()) {
            try {
                conn.setAutoCommit(false);
                List<T> insertedVersions = insert(persist, newVersions, conn);
                conn.commit();
                return insertedVersions;
            } catch (DAOException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.FAIL_TO_INSERT, e.getMessage());
        }
    }

    public static <T> List<T> insert(Insert<T> persist, List<T> newVersions, Connection conn) throws DAOException
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
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.FAIL_TO_INSERT, e.getMessage());
        }
    }

    private static <T> Map<T, T> createMapFromList(List<T> domainObjects)
    {
        Map<T, T> domainObjectMap = new HashMap<>(domainObjects.size());
        for (T domainObject : domainObjects) {
            domainObjectMap.put(domainObject, domainObject);
        }
        return domainObjectMap;
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Set<U> createDependentObjectToSelectSet(List<T> prototypes, String accessorName)
        throws DAOException
    {
        Set<U> toSelectSet = new HashSet<>(prototypes.size());
        try {
            if (prototypes.size() > 0) {
                Class<?> prototypesClass = prototypes.get(0).getClass();
                Method dependentObjectAccessorMethod = prototypesClass.getDeclaredMethod(accessorName);
                for (T prototype : prototypes) {
                    U dependentObject = (U) dependentObjectAccessorMethod.invoke(prototype);
                    toSelectSet.add(dependentObject);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.SQL_ERROR, e.getMessage());
        }
        return toSelectSet;
    }

    @SuppressWarnings("unchecked")
    public static <T, U> void setSelectedDependentObjectsInPrototypes(List<T> prototypes,
            List<U> selectedDomainObjects, String accessorName, String mutatorName) throws DAOException
    {
        if (prototypes.size() > 0) {
            Map<U, U> selectedDomainObjectMap = PersistManager.createMapFromList(selectedDomainObjects);
            try {
                Class<?> prototypesClass = prototypes.get(0).getClass();
                Method dependentObjectAccessorMethod = prototypesClass.getDeclaredMethod(accessorName);
                Method dependentObjectMutatorMethod = prototypesClass.getDeclaredMethod(mutatorName);
                // Set selected domain objects into respective prototype objects.
                for (T prototype : prototypes) {
                    U prototypeDependentObject = (U) dependentObjectAccessorMethod.invoke(prototype);
                    if (!selectedDomainObjectMap.containsKey(prototypeDependentObject)) {
                        String errorMessage = String.format(Messages.SELECT_DEPENDENT_OBJECT_FAILED_MESSAGE,
                                prototypeDependentObject.toString());
                        log.error(errorMessage);
                        throw new DAOException(DAOException.ErrorCode.SQL_ERROR, errorMessage);
                    }
                    U selectedDomainObject = selectedDomainObjectMap.get(prototypeDependentObject);
                    dependentObjectMutatorMethod.invoke(prototype, selectedDomainObject);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error(e);
                throw new DAOException(DAOException.ErrorCode.SQL_ERROR, e.getMessage());
	        }
        }
    }

    public static <T> List<T> select(Select<T> persist, List<T> prototypes) throws DAOException
    {
        try (Connection conn = persist.createConnection();) {
            conn.setAutoCommit(false);
            List<T> selectedVersions = select(persist, prototypes, conn);
            conn.commit();
            return selectedVersions;
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.SQL_ERROR, e.getMessage());
        }
    }

    public static <T> List<T> select(Select<T> persist, List<T> prototypes, Connection conn) throws DAOException
    {
        try {
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
            } catch (SQLException e) {
                log.error(e);
                throw new DAOException(DAOException.ErrorCode.SQL_ERROR, e.getMessage());
            }
        } catch (DAOException e) {
            log.error(e);
            throw e;
        }
    }

    private static <T> void updateInternal(Statement stmt, Persist<T> persist, List<T> prototypes) throws DAOException,
        SQLException
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
            throw new DAOException(DAOException.ErrorCode.UPDATE_FAILED, String.format(Messages.UPDATE_FAILED_MESSAGE,
                    updateFailedCount));
        }
    }

    public static <T> void update(Update<T> persist, List<T> newVersions) throws DAOException
    {
        try (Connection conn = persist.createConnection();) {
            try {
                conn.setAutoCommit(false);
                update(persist, newVersions, conn);
                conn.commit();
            } catch (DAOException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    public static <T> void update(Update<T> persist, List<T> newVersions, Connection conn) throws DAOException
    {
        try (CallableStatement stmt = conn.prepareCall(persist.getUpdateStoredProcedure());) {
            for (T newVersion : newVersions) {
                persist.beforeUpdateInitializeStatementFromDomainObject(stmt, newVersion);
                stmt.addBatch();
            }
            updateInternal(stmt, persist, newVersions);
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    /**
     * Delete is simply an update marking the user's active flag to false.
     * 
     * @param persist
     * @param prototypes
     * @throws DAOException
     */
    public static <T> void delete(Delete<T> persist, List<T> prototypes) throws DAOException
    {
        try (Connection conn = persist.createConnection();) {
            try {
                conn.setAutoCommit(false);
                delete(persist, prototypes, conn);
                conn.commit();
            } catch (DAOException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    /**
     * Delete is simply an update marking the user's active flag to false.
     * 
     * @param persist
     * @param prototypes
     * @throws DAOException
     */
    public static <T> void delete(Delete<T> persist, List<T> prototypes, Connection conn) throws DAOException
    {
        try (CallableStatement stmt = conn.prepareCall(persist.getDeleteStoredProcedure());) {
            for (T prototype : prototypes) {
                persist.beforeDeleteInitializeStatementFromDomainObject(stmt, prototype);
                stmt.addBatch();
            }
            updateInternal(stmt, persist, prototypes);
        } catch (SQLException e) {
            log.error(e);
            throw new DAOException(DAOException.ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }
}
