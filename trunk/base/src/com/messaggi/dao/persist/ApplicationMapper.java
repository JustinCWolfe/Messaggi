package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.List;

import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.domain.Application;
import com.messaggi.domain.User;

public class ApplicationMapper implements Save<Application>
{
    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveApplication(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<Application> domainObjects)
        throws Exception
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
}

