package com.messaggi.persistence.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.messaggi.persistence.domain.User;

public class DAOTestHelper
{
    private static final String SELECT_TEST_USER_STATEMENT = "select * from public.user where id = ?";
    private static final String INSERT_TEST_USER_STATEMENT = "insert into public.user (name, email, phone, phone_parsed, password, locale, active) values (?,?,?,?,?,?,?)";
    private static final String DELETE_TEST_USER_STATEMENT = "delete from public.user where id = ?";

    public static List<User> selectTestUser(User... users) throws Exception
    {
        List<User> selectedUsers = new ArrayList<>();
        if (users.length > 0) {
            try (Connection conn = PostgreSQLDAOFactory.createConnection();
                    PreparedStatement stmt = conn.prepareStatement(SELECT_TEST_USER_STATEMENT);) {
                for (User user : users) {
                    stmt.setLong(1, user.getId());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        User selectedUser = new User();
                        selectedUser.setId(rs.getLong(1));
                        selectedUser.setName(rs.getString(2));
                        selectedUser.setEmail(rs.getString(3));
                        selectedUser.setPhone(rs.getString(4));
                        selectedUser.setPhoneParsed(rs.getString(5));
                        selectedUser.setPassword(rs.getString(6));
                        selectedUser.setLocale(Locale.forLanguageTag(rs.getString(7)));
                        selectedUser.setActive(rs.getBoolean(8));
                        selectedUsers.add(selectedUser);
                    }
                }
            }
        }
        return selectedUsers;
    }

    public static void insertTestUser(User... users) throws Exception
    {
        if (users.length > 0) {
            try (Connection conn = PostgreSQLDAOFactory.createConnection();
                    PreparedStatement stmt = conn.prepareStatement(INSERT_TEST_USER_STATEMENT,
                            Statement.RETURN_GENERATED_KEYS);) {
                for (User user : users) {
                    stmt.setString(1, user.getName());
                    stmt.setString(2, user.getEmail());
                    stmt.setString(3, user.getPhone());
                    stmt.setString(4, user.getPhoneParsed());
                    stmt.setString(5, user.getPassword());
                    stmt.setString(6, user.getLocale().toString());
                    stmt.setBoolean(7, user.getActive());
                    stmt.addBatch();
                }
                stmt.executeBatch();
                ResultSet rs = stmt.getGeneratedKeys();
                int userIndex = 0;
                while (rs.next()) {
                    users[userIndex++].setId(rs.getLong(1));
                }
            }
        }
    }

    public static void deleteTestUser(Long... ids) throws Exception
    {
        if (ids.length > 0) {
            try (Connection conn = PostgreSQLDAOFactory.createConnection();
                    PreparedStatement stmt = conn.prepareStatement(DELETE_TEST_USER_STATEMENT);) {
                for (Long id : ids) {
                    if (id != null) {
                        stmt.setLong(1, id);
                        stmt.addBatch();
                    }
                }
                stmt.executeBatch();
            }
        }
    }
}

