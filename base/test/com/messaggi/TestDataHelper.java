package com.messaggi;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Locale;

import com.messaggi.dao.PersistManager;
import com.messaggi.domain.User;

public class TestDataHelper
{
    public static class User1
    {
        public static Integer ID = null;

        public static final String NAME = "Test User 1";

        public static final String EMAIL = "test_user1@yahoo.com";

        public static final String PHONE = "617-549-2403";

        public static final String PASSWD_HASH = "test_user_1_pwd";

        public static final String PASSWD_SALT = "some_salt1";

        public static final Locale LOCALE = Locale.US;

        public static User getDomainObject()
        {
            User u = new User();
            u.setId(ID);
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
        public static Integer ID = null;

        public static final String NAME = "Test User 2";

        public static final String EMAIL = "test_user2@yahoo.com";

        public static final String PHONE = "617-549-8277";

        public static final String PASSWD_HASH = "test_user_2_pwd";

        public static final String PASSWD_SALT = "some_salt2";

        public static final Locale LOCALE = Locale.ITALY;

        public static User getDomainObject()
        {
            User u = new User();
            u.setId(ID);
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
    }

    public static Connection getConnection() throws Exception
    {
        Method getConnectionMethod = PersistManager.class.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);
        return (Connection) getConnectionMethod.invoke(null);
    }

    public static void createUser1() throws Exception
    {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareCall(Statements.CREATE_USER_STMT);) {
                stmt.setString(1, User1.NAME);
                stmt.setString(2, User1.EMAIL);
                stmt.setString(3, User1.PHONE);
                stmt.setString(4, User1.PASSWD_HASH);
                stmt.setString(5, User1.PASSWD_SALT);
                stmt.setString(6, User1.LOCALE.toLanguageTag());
                stmt.setBoolean(7, true);
                stmt.execute();
            }
        }
    }

    public static void deleteUser1() throws Exception
    {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareCall(Statements.DELETE_USER_STMT);) {
                stmt.setInt(1, User1.ID);
                stmt.setString(2, User1.EMAIL);
                stmt.execute();
            }
        }
    }

    public static void createUser2() throws Exception
    {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareCall(Statements.CREATE_USER_STMT);) {
                stmt.setString(1, User2.NAME);
                stmt.setString(2, User2.EMAIL);
                stmt.setString(3, User2.PHONE);
                stmt.setString(4, User2.PASSWD_HASH);
                stmt.setString(5, User2.PASSWD_SALT);
                stmt.setString(6, User2.LOCALE.toLanguageTag());
                stmt.setBoolean(7, true);
                stmt.execute();
            }
        }
    }

    public static void deleteUser2() throws Exception
    {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareCall(Statements.DELETE_USER_STMT);) {
                stmt.setInt(1, User2.ID);
                stmt.setString(2, User2.EMAIL);
                stmt.execute();
            }
        }
    }
}

