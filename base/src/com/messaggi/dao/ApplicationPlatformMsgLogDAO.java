package com.messaggi.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import com.messaggi.dao.PersistManager.Save;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatformMsgLog;

public class ApplicationPlatformMsgLogDAO implements Save<ApplicationPlatformMsgLog>
{
    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveApplicationPlatformMsgLog(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs,
            List<ApplicationPlatformMsgLog> domainObjects) throws SQLException
    {
        while (rs.next()) {
            ApplicationPlatformMsgLog domainObject = new ApplicationPlatformMsgLog();
            domainObject.setId(rs.getInt("ID"));
            domainObject.setDate(rs.getDate("Date"));
            domainObject.setMsgCount(rs.getInt("MsgCount"));
            ApplicationPlatform applicationPlatform = new ApplicationPlatform();
            applicationPlatform.setId(rs.getInt("ApplicationPlatformID"));
            domainObject.setApplicationPlatform(applicationPlatform);
            domainObjects.add(domainObject);
        }
    }

    public List<ApplicationPlatformMsgLog> saveApplicationPlatformMsgLog(List<ApplicationPlatformMsgLog> newVersions)
        throws NamingException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException,
        InstantiationException, JAXBException, IOException
    {
        return PersistManager.save(this, newVersions);
    }
}

