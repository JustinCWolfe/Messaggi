package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.messaggi.dao.persist.ObjectRelationalMapper.Get;
import com.messaggi.dao.persist.ObjectRelationalMapper.Save;
import com.messaggi.domain.Application;
import com.messaggi.domain.User;

public class UserMapper implements Get<User>, Save<User>
{
    // Get implementation
    @Override
    public String getGetStoredProcedure()
    {
        return "{call GetUser(?)}";
    }

    @Override
    public void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<User> domainObjects) throws Exception
    {
        HashMap<Integer, User> userMap = new HashMap<>();
        while (rs.next()) {
            User domainObject = null;
            Integer userID = rs.getInt("ID");
            if (!userMap.containsKey(userID)) {
                domainObject = new User();
                domainObject.setId(userID);
                domainObject.setName(rs.getString("Name"));
                domainObject.setEmail(rs.getString("Email"));
                domainObject.setPhone(rs.getString("Phone"));
                domainObject.setPasswordHashAsBinary(rs.getBytes("PasswordHash"));
                domainObject.setPasswordSalt(rs.getString("PasswordSalt"));
                domainObject.setLocale(Locale.forLanguageTag(rs.getString("Locale")));
                domainObject.setActive(rs.getBoolean("Active"));
            } else {
                domainObject = userMap.get(userID);
            }

            Application a = new Application();
            a.setId(rs.getInt("ApplicationID"));
            a.setName(rs.getString("ApplicationName"));
            a.setActive(rs.getBoolean("ApplicationActive"));
            a.setUser(domainObject);

            if (domainObject.getApplications() == null) {
                domainObject.setApplications(new HashSet<Application>());
            }
            domainObject.getApplications().add(a);

            userMap.put(userID, domainObject);
        }
        domainObjects.addAll(userMap.values());
    }

    // Save implementation
    @Override
    public String getSaveStoredProcedure()
    {
        return "{call SaveUser(?)}";
    }

    @Override
    public void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<User> domainObjects) throws Exception
    {
        while (rs.next()) {
            User domainObject = new User();
            domainObject.setId(rs.getInt("ID"));
            domainObject.setName(rs.getString("Name"));
            domainObject.setEmail(rs.getString("Email"));
            domainObject.setPhone(rs.getString("Phone"));
            domainObject.setPasswordHashAsBinary(rs.getBytes("PasswordHash"));
            domainObject.setPasswordSalt(rs.getString("PasswordSalt"));
            domainObject.setLocale(Locale.forLanguageTag(rs.getString("Locale")));
            domainObject.setActive(rs.getBoolean("Active"));
            domainObjects.add(domainObject);
        }
    }
}

