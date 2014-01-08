package com.messaggi.dao.persist;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.GetAll;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.util.JAXBHelper;

public class PersistManager
{
    private class Messages
    {
        public static final String DATASOURCE_NOT_FOUND_MESSAGE = "Datasource '%s' not found.";
    }

    public static final String MESSAGGI_DATABASE_JNDI_NAME = "java:/comp/env/jdbc/Messaggi";

    private static Connection getConnection() throws Exception
    {
        DataSource ds = (DataSource) InitialContext.doLookup(MESSAGGI_DATABASE_JNDI_NAME);
        if (ds == null) {
            throw new SQLException(String.format(Messages.DATASOURCE_NOT_FOUND_MESSAGE, ""));
        }
        return ds.getConnection();
    }

    private static <T> void initializeStatementFromDomainObjects(PreparedStatement stmt, T[] domainObjects)
        throws Exception
    {
        stmt.setString(1, JAXBHelper.objectToXML(domainObjects));
    }

    public static <T> List<T> get(Get<T> mapper, T[] prototypes) throws Exception
    {
        try (Connection conn = getConnection();) {
            try (CallableStatement stmt = conn.prepareCall(mapper.getGetStoredProcedure());) {
                initializeStatementFromDomainObjects(stmt, prototypes);
                List<T> selectedDomainObjects = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    mapper.afterGetInitializeDomainObjectsFromResultSet(rs, selectedDomainObjects);
                }
                return selectedDomainObjects;
            }
        }
    }

    public static <T> List<T> getAll(GetAll<T> mapper) throws Exception
    {
        try (Connection conn = getConnection();) {
            try (CallableStatement stmt = conn.prepareCall(mapper.getGetAllStoredProcedure());) {
                List<T> selectedDomainObjects = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    mapper.afterGetAllInitializeDomainObjectsFromResultSet(rs, selectedDomainObjects);
                }
                return selectedDomainObjects;
            }
        }
    }

    public static <T> List<T> save(Save<T> mapper, T[] newVersions) throws Exception
    {
        try (Connection conn = getConnection()) {
            try (CallableStatement stmt = conn.prepareCall(mapper.getSaveStoredProcedure());) {
                initializeStatementFromDomainObjects(stmt, newVersions);
                List<T> savedVersions = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery();) {
                    mapper.afterSaveInitializeDomainObjectsFromResultSet(rs, savedVersions);
                }
                return savedVersions;
            }
        }
    }
}
