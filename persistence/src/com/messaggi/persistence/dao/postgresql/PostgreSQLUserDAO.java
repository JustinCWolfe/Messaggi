package com.messaggi.persistence.dao.postgresql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messaggi.persistence.dao.UserDAO;
import com.messaggi.persistence.domain.User;

public class PostgreSQLUserDAO implements UserDAO
{
    private static Log log = LogFactory.getLog(PostgreSQLUserDAO.class);

    @Override
    public List<User> insertUser(List<User> newVersions) throws SQLException
    {
        List<User> users = new ArrayList<>();
        String insertStoredProcedure = "{call m_create_user(?,?,?,?,?)}";
        try (Connection conn = PostgreSQLDAOFactory.createConnection();
                CallableStatement stmt = conn.prepareCall(insertStoredProcedure);) {
            for (User user : newVersions) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPhone());
                stmt.setString(4, user.getPassword());
                // IS THIS RIGHT?
                stmt.setString(5, user.getLocale().toString());
                stmt.addBatch();
            }
            //try (ResultSet rs = stmt.executeBatch();) {
            //while (rs.next()) {
                    //users.add(new User(rs.getInt("id"), rs.getString("name")));
            //}
            //}
        } catch (SQLException e) {
            log.error(e);
        }
        return users;
    }

    @Override
    public List<User> updateUser(List<User> newVersions)
    {
        return null;
    }

    @Override
    public List<User> selectUser(List<User> prototypes)
    {
        return null;
    }
}

