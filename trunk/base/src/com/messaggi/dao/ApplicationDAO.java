package com.messaggi.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import com.messaggi.dao.PersistManager.Save;
import com.messaggi.domain.Application;
import com.messaggi.domain.User;

public class ApplicationDAO implements Save<Application>
{
    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveApplication(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
        throws SQLException
    {
        while (rs.next()) {
            Application domainObject = new Application();
            domainObject.setId(rs.getInt("ID"));
            domainObject.setName(rs.getString("Name"));
            domainObject.setActive(rs.getBoolean("Active"));
            User user = new User();
            user.setId(rs.getInt("UserID"));
            domainObject.setUser(user);
            domainObjects.add(domainObject);
        }
    }

    public List<Application> saveApplication(List<Application> newVersions) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException,
        JAXBException, IOException
    {
        return PersistManager.save(this, newVersions);
    }
}
