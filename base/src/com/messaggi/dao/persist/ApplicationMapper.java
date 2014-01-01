package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
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
        HashMap<Integer, User> userMap = new HashMap<>();
        while (rs.next()) {
            Application domainObject = new Application();

            int id = rs.getInt("ID");
            domainObject.setId(rs.wasNull() ? null : id);

            domainObject.setName(rs.getString("Name"));

            boolean active = rs.getBoolean("Active");
            domainObject.setActive(rs.wasNull() ? null : active);

            int userId = rs.getInt("UserID");
            Integer userIdObj = rs.wasNull() ? null : userId;
            if (userIdObj != null) {
                User user = null;
                if (!userMap.containsKey(userIdObj)) {
                    user = new User();
                    user.setId(userIdObj);
                    user.setApplications(new HashSet<Application>());
                    userMap.put(userIdObj, user);
                } else {
                    user = userMap.get(userIdObj);
                }
                user.getApplications().add(domainObject);
                domainObject.setUser(user);
            }

            domainObjects.add(domainObject);
        }
    }
}

