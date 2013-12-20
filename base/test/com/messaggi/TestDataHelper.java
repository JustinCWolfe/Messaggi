package com.messaggi;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Locale;

import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.DomainHelper;
import com.messaggi.domain.User;

public class TestDataHelper
{
    public static class User1
    {
        public static final String NAME = "Unit Test User 1";

        public static final String EMAIL = "unit_test_user1@yahoo.com";

        public static final String PHONE = "617-549-2403";

        public static final String PASSWD_HASH = "unit_test_user_1_pwd";

        public static final String PASSWD_SALT = "some_salt1";

        public static final Locale LOCALE = Locale.US;

        public static User getDomainObject()
        {
            User u = new User();
            u.setName(NAME);
            u.setEmail(EMAIL);
            u.setPhone(PHONE);
            u.setPasswordHash(PASSWD_HASH);
            u.setPasswordSalt(PASSWD_SALT);
            u.setLocale(LOCALE);
            return u;
        }
    }

    public static class User2
    {
        public static final String NAME = "Unit Test User 2";

        public static final String EMAIL = "unit_test_user2@yahoo.com";

        public static final String PHONE = "617-549-8277";

        public static final String PASSWD_HASH = "unit_test_user_2_pwd";

        public static final String PASSWD_SALT = "some_salt2";

        public static final Locale LOCALE = Locale.ITALY;

        public static User getDomainObject()
        {
            User u = new User();
            u.setName(NAME);
            u.setEmail(EMAIL);
            u.setPhone(PHONE);
            u.setPasswordHash(PASSWD_HASH);
            u.setPasswordSalt(PASSWD_SALT);
            u.setLocale(LOCALE);
            return u;
        }
    }

    private static class Statements
    {
        private static final String CREATE_USER_STMT = "insert into dbo.[User] (Name, Email, Phone, PasswordHash, PasswordSalt, Locale, Active) values (?,?,?,?,?,?,?)";

        private static final String DELETE_USER_STMT = "delete from dbo.[User] where ID = ? or Email = ?";

        private static final String GET_USER_ID_STMT = "select ID from dbo.[User] where Email = ?";
    }

    public static Connection getConnection() throws Exception
    {
        Method getConnectionMethod = PersistManager.class.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);
        return (Connection) getConnectionMethod.invoke(null);
    }

    public static void createUser(User u) throws Exception
    {
        if (u == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.CREATE_USER_STMT);) {
                stmt.setString(1, u.getName());
                stmt.setString(2, u.getEmail());
                stmt.setString(3, u.getPhone());
                stmt.setBytes(4, DomainHelper.encodeBase64Image(u.getPasswordHash()));
                stmt.setString(5, u.getPasswordSalt());
                stmt.setString(6, u.getLocale().toLanguageTag());
                stmt.setBoolean(7, true);
                stmt.execute();
            }
            try (PreparedStatement stmt = conn.prepareStatement(Statements.GET_USER_ID_STMT);) {
                stmt.setString(1, u.getEmail());
                try (ResultSet rs = stmt.executeQuery();) {
                    while (rs.next()) {
                        u.setId(rs.getInt(1));
                        break;
                    }
                }
            }
        }
    }

    public static void deleteUser(User u) throws Exception
    {
        if (u == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.DELETE_USER_STMT);) {
                if (u.getId() != null) {
                    stmt.setInt(1, u.getId());
                } else {
                    stmt.setNull(1, Types.NULL);
                }
                stmt.setString(2, u.getEmail());
                stmt.execute();
            }
        }
    }
}

