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
        HashMap<Integer, Application> applicationMap = new HashMap<>();
        while (rs.next()) {
            User domainObject = null;
            int id = rs.getInt("ID");
            Integer idObj = rs.wasNull() ? null : id;
            if (idObj != null) {
                if (!userMap.containsKey(idObj)) {
                    domainObject = new User();
                    domainObject.setId(idObj);
                    domainObject.setName(rs.getString("Name"));
                    domainObject.setEmail(rs.getString("Email"));
                    domainObject.setPhone(rs.getString("Phone"));

                    byte[] passwordHash = rs.getBytes("PasswordHash");
                    domainObject.setPasswordHashAsBinary(rs.wasNull() ? null : passwordHash);

                    domainObject.setPasswordSalt(rs.getString("PasswordSalt"));
                    domainObject.setLocale(Locale.forLanguageTag(rs.getString("Locale")));

                    boolean active = rs.getBoolean("Active");
                    domainObject.setActive(rs.wasNull() ? null : active);

                    domainObject.setApplications(new HashSet<Application>());
                    userMap.put(idObj, domainObject);
                } else {
                    domainObject = userMap.get(idObj);
                }

                int applicationId = rs.getInt("ApplicationID");
                Integer applicationIdObj = rs.wasNull() ? null : applicationId;
                if (applicationIdObj != null) {
                    Application application = null;
                    if (!applicationMap.containsKey(applicationId)) {
                        application = new Application();
                        application.setId(applicationIdObj);
                        application.setName(rs.getString("ApplicationName"));

                        boolean active = rs.getBoolean("ApplicationActive");
                        application.setActive(rs.wasNull() ? null : active);

                        application.setUser(domainObject);
                        applicationMap.put(applicationIdObj, application);
                    } else {
                        application = applicationMap.get(applicationIdObj);
                    }
                    domainObject.getApplications().add(application);
                }
            }
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
            domainObject.setApplications(new HashSet<Application>());
            domainObjects.add(domainObject);
        }
    }
}

