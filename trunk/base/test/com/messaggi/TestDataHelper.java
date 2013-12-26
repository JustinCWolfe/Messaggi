package com.messaggi;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Locale;
import java.util.UUID;

import com.messaggi.dao.persist.PersistManager;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatform.Platform;
import com.messaggi.domain.DomainHelper;
import com.messaggi.domain.User;

public class TestDataHelper
{
    public static class Application1
    {
        public static final String NAME = "Unit Test Application 1";

        public static Application getDomainObject()
        {
            Application a = new Application();
            a.setName(NAME);
            return a;
        }
    }

    public static class Application2
    {
        public static final String NAME = "Unit Test Application 2";

        public static Application getDomainObject()
        {
            Application a = new Application();
            a.setName(NAME);
            return a;
        }
    }

    public static class Application3
    {
        public static final String NAME = "Unit Test Application 3";

        public static Application getDomainObject()
        {
            Application a = new Application();
            a.setName(NAME);
            return a;
        }
    }

    public static class ApplicationPlatform1
    {
        public static final UUID TOKEN = UUID.randomUUID();

        public static final Platform PLATFORM = Platform.ANDROID;

        public static ApplicationPlatform getDomainObject()
        {
            ApplicationPlatform ap = new ApplicationPlatform();
            ap.setPlatform(PLATFORM);
            ap.setToken(TOKEN);
            return ap;
        }
    }

    public static class ApplicationPlatform2
    {
        public static final UUID TOKEN = UUID.randomUUID();

        public static final Platform PLATFORM = Platform.IOS;

        public static ApplicationPlatform getDomainObject()
        {
            ApplicationPlatform ap = new ApplicationPlatform();
            ap.setPlatform(PLATFORM);
            ap.setToken(TOKEN);
            return ap;
        }
    }

    public static class ApplicationPlatform3
    {
        public static final UUID TOKEN = UUID.randomUUID();

        public static final Platform PLATFORM = Platform.WINDOWS;

        public static ApplicationPlatform getDomainObject()
        {
            ApplicationPlatform ap = new ApplicationPlatform();
            ap.setPlatform(PLATFORM);
            ap.setToken(TOKEN);
            return ap;
        }
    }

    public static class ApplicationPlatform4
    {
        public static final UUID TOKEN = UUID.randomUUID();

        public static final Platform PLATFORM = Platform.IOS;

        public static ApplicationPlatform getDomainObject()
        {
            ApplicationPlatform ap = new ApplicationPlatform();
            ap.setPlatform(PLATFORM);
            ap.setToken(TOKEN);
            return ap;
        }
    }

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
        private static final String CREATE_APP_STMT = "insert into dbo.[Application] (UserID, Name) values (?,?)";

        private static final String CREATE_APP_PLAT_STMT = "insert into dbo.[ApplicationPlatform] (ApplicationID, PlatformCode, Token) values (?,?,?)";

        private static final String CREATE_USER_STMT = "insert into dbo.[User] (Name, Email, Phone, PasswordHash, PasswordSalt, Locale) values (?,?,?,?,?,?)";

        private static final String DELETE_APP_STMT = "delete from dbo.[Application] where ID = ?";

        private static final String DELETE_APP_PLAT_STMT = "delete from dbo.[ApplicationPlatform] where ID = ?";

        private static final String DELETE_USER_STMT = "delete from dbo.[User] where ID = ? or Email = ?";
    }

    public static Connection getConnection() throws Exception
    {
        Method getConnectionMethod = PersistManager.class.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);
        return (Connection) getConnectionMethod.invoke(null);
    }

    public static void createApplication(Application a) throws Exception
    {
        if (a == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.CREATE_APP_STMT,
                    Statement.RETURN_GENERATED_KEYS);) {
                stmt.setInt(1, a.getUserId());
                stmt.setString(2, a.getName());
                stmt.execute();
                try (ResultSet rs = stmt.getGeneratedKeys();) {
                    while (rs.next()) {
                        a.setId(rs.getInt(1));
                        break;
                    }
                }
            }
        }
    }

    public static void createApplicationPlatform(ApplicationPlatform ap) throws Exception
    {
        if (ap == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.CREATE_APP_PLAT_STMT,
                    Statement.RETURN_GENERATED_KEYS);) {
                stmt.setInt(1, ap.getApplicationId());
                stmt.setString(2, ap.getPlatform().toString());
                stmt.setString(3, ap.getToken().toString());
                stmt.execute();
                try (ResultSet rs = stmt.getGeneratedKeys();) {
                    while (rs.next()) {
                        ap.setId(rs.getInt(1));
                        break;
                    }
                }
            }
        }
    }

    public static void createUser(User u) throws Exception
    {
        if (u == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.CREATE_USER_STMT,
                    Statement.RETURN_GENERATED_KEYS);) {
                stmt.setString(1, u.getName());
                stmt.setString(2, u.getEmail());
                stmt.setString(3, u.getPhone());
                stmt.setBytes(4, DomainHelper.encodeBase64Image(u.getPasswordHash()));
                stmt.setString(5, u.getPasswordSalt());
                stmt.setString(6, u.getLocale().toLanguageTag());
                stmt.execute();
                try (ResultSet rs = stmt.getGeneratedKeys();) {
                    while (rs.next()) {
                        u.setId(rs.getInt(1));
                        break;
                    }
                }
            }
        }
    }

    public static void deleteApplication(Application a) throws Exception
    {
        if (a == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.DELETE_APP_STMT);) {
                stmt.setInt(1, a.getId());
                stmt.execute();
            }
        }
    }

    public static void deleteApplicationPlatform(ApplicationPlatform ap) throws Exception
    {
        if (ap == null) {
            return;
        }
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(Statements.DELETE_APP_PLAT_STMT);) {
                stmt.setInt(1, ap.getId());
                stmt.execute();
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

