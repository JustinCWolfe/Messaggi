package com.messaggi.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.bind.JAXBException;

import com.messaggi.util.JAXBHelper;

public class PersistManager
{
    public interface Get<T>
    {
        String getGetStoredProcedure(T[] prototypes) throws SQLException;

        void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws SQLException;
    }

    public interface Save<T>
    {
        String getSaveStoredProcedure();

        void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws SQLException;
    }

    private class Messages
    {
        public static final String DATASOURCE_NOT_FOUND_MESSAGE = "Datasource '%s' not found.";
    }

    public static final String MESSAGGI_DATABASE_JNDI_NAME = "java:/comp/env/jdbc/Messaggi";

    private static Connection getConnection() throws NamingException, SQLException
    {
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup(MESSAGGI_DATABASE_JNDI_NAME);
        if (ds == null) {
            throw new SQLException(String.format(Messages.DATASOURCE_NOT_FOUND_MESSAGE, ""));
        }
        return ds.getConnection();
    }

    private static <T> void initializeStatementFromDomainObjects(PreparedStatement stmt, T[] domainObjects)
        throws SQLException, JAXBException, IOException
    {
        SQLXML xmlVar = stmt.getConnection().createSQLXML();
        StringBuilder sb = new StringBuilder();
        for (T domainObject : domainObjects) {
            sb.append(JAXBHelper.objectToXML(domainObject));
        }
        xmlVar.setString(sb.toString());
        stmt.setObject(1, xmlVar);
    }

    public static <T> List<T> get(Get<T> persist, T[] prototypes) throws NamingException, SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException,
        JAXBException, IOException
    {
        try (Connection conn = getConnection();) {
            try (CallableStatement stmt = conn.prepareCall(persist.getGetStoredProcedure(prototypes));) {
                initializeStatementFromDomainObjects(stmt, prototypes);
                List<T> selectedDomainObjects = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    persist.afterGetInitializeDomainObjectsFromResultSet(rs, selectedDomainObjects);
                }
                return selectedDomainObjects;
            }
        }
    }

    public static <T> List<T> save(Save<T> persist, T[] newVersions) throws NamingException, SQLException,
        IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException,
        JAXBException, IOException
    {
        try (Connection conn = getConnection()) {
            try (CallableStatement stmt = conn.prepareCall(persist.getSaveStoredProcedure());) {
                initializeStatementFromDomainObjects(stmt, newVersions);
                List<T> savedVersions = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    persist.afterSaveInitializeDomainObjectsFromResultSet(rs, savedVersions);
                }
                return savedVersions;
            }
        }
    }
}
