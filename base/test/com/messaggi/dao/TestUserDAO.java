package com.messaggi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.TestDataHelper.User2;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestUserDAO extends MessaggiTestCase
{
    private User user1;

    private User user2;

    private UserDAO userDAO;

    @Override
    @Before
    public void setUp() throws Exception
    {
        userDAO = new UserDAO();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteUser(user1);
        TestDataHelper.deleteUser(user2);
    }

    @Test
    public void testGetUserById() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();
        TestDataHelper.createUser(user1);
        TestDataHelper.createUser(user2);
        user1.setEmail(null);
        user2.setEmail(null);
        User[] users = { user1, user2, user1, user2 };
        List<User> retrievedUsers = userDAO.getUser(users);
        User retrievedUser1 = null, retrievedUser2 = null;
        for (User user : retrievedUsers) {
            if (user.getEmail().equals(User1.EMAIL)) {
                retrievedUser1 = user;
            } else {
                retrievedUser2 = user;
            }
        }
        assertEquals(2, retrievedUsers.size());
        assertEquals(user1.getId(), retrievedUser1.getId());
        assertEquals(User1.NAME, retrievedUser1.getName());
        assertEquals(User1.EMAIL, retrievedUser1.getEmail());
        assertEquals(User1.PHONE, retrievedUser1.getPhone());
        assertEquals(User1.PASSWD_HASH, retrievedUser1.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, retrievedUser1.getPasswordSalt());
        assertEquals(User1.LOCALE, retrievedUser1.getLocale());
        assertEquals(user2.getId(), retrievedUser2.getId());
        assertEquals(User2.NAME, retrievedUser2.getName());
        assertEquals(User2.EMAIL, retrievedUser2.getEmail());
        assertEquals(User2.PHONE, retrievedUser2.getPhone());
        assertEquals(User2.PASSWD_HASH, retrievedUser2.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, retrievedUser2.getPasswordSalt());
        assertEquals(User2.LOCALE, retrievedUser2.getLocale());
    }

    @Test
    public void testGetUserByEmail() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();
        TestDataHelper.createUser(user1);
        TestDataHelper.createUser(user2);
        User noIDUser1 = User1.getDomainObject();
        User noIDUser2 = User2.getDomainObject();
        User[] users = { noIDUser1, noIDUser2, noIDUser1, noIDUser2 };
        List<User> retrievedUsers = userDAO.getUser(users);
        User retrievedUser1 = null, retrievedUser2 = null;
        for (User user : retrievedUsers) {
            if (user.getEmail().equals(User1.EMAIL)) {
                retrievedUser1 = user;
            } else {
                retrievedUser2 = user;
            }
        }
        assertEquals(2, retrievedUsers.size());
        assertEquals(user1.getId(), retrievedUser1.getId());
        assertEquals(User1.NAME, retrievedUser1.getName());
        assertEquals(User1.EMAIL, retrievedUser1.getEmail());
        assertEquals(User1.PHONE, retrievedUser1.getPhone());
        assertEquals(User1.PASSWD_HASH, retrievedUser1.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, retrievedUser1.getPasswordSalt());
        assertEquals(User1.LOCALE, retrievedUser1.getLocale());
        assertEquals(user2.getId(), retrievedUser2.getId());
        assertEquals(User2.NAME, retrievedUser2.getName());
        assertEquals(User2.EMAIL, retrievedUser2.getEmail());
        assertEquals(User2.PHONE, retrievedUser2.getPhone());
        assertEquals(User2.PASSWD_HASH, retrievedUser2.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, retrievedUser2.getPasswordSalt());
        assertEquals(User2.LOCALE, retrievedUser2.getLocale());
    }

    @Test
    public void testGetUserByIdAndEmail() throws Exception
    {
        user1 = User1.getDomainObject();
        User[] users1 = { user1 };
        List<User> retrievedUsers = userDAO.getUser(users1);
        assertEquals(0, retrievedUsers.size());

        user2 = User2.getDomainObject();
        User[] users2 = { user1, user2 };
        List<User> retrievedUsers2 = userDAO.getUser(users2);
        assertEquals(0, retrievedUsers2.size());

        TestDataHelper.createUser(user1);
        User[] users3 = { user1, user1, user1, user2, user2 };
        List<User> retrievedUsers3 = userDAO.getUser(users3);
        User retrievedUser31 = retrievedUsers3.get(0);
        assertEquals(1, retrievedUsers3.size());
        assertEquals(user1.getId(), retrievedUser31.getId());
        assertEquals(User1.NAME, retrievedUser31.getName());
        assertEquals(User1.EMAIL, retrievedUser31.getEmail());
        assertEquals(User1.PHONE, retrievedUser31.getPhone());
        assertEquals(User1.PASSWD_HASH, retrievedUser31.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, retrievedUser31.getPasswordSalt());
        assertEquals(User1.LOCALE, retrievedUser31.getLocale());

        TestDataHelper.createUser(user2);
        List<User> retrievedUsers4 = userDAO.getUser(users3);
        User retrievedUser41 = null, retrievedUser42 = null;
        for (User user : retrievedUsers4) {
            if (user.getEmail().equals(User1.EMAIL)) {
                retrievedUser41 = user;
            } else {
                retrievedUser42 = user;
            }
        }
        assertEquals(2, retrievedUsers4.size());
        assertEquals(user1.getId(), retrievedUser41.getId());
        assertEquals(User1.NAME, retrievedUser41.getName());
        assertEquals(User1.EMAIL, retrievedUser41.getEmail());
        assertEquals(User1.PHONE, retrievedUser41.getPhone());
        assertEquals(User1.PASSWD_HASH, retrievedUser41.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, retrievedUser41.getPasswordSalt());
        assertEquals(User1.LOCALE, retrievedUser41.getLocale());
        assertEquals(user2.getId(), retrievedUser42.getId());
        assertEquals(User2.NAME, retrievedUser42.getName());
        assertEquals(User2.EMAIL, retrievedUser42.getEmail());
        assertEquals(User2.PHONE, retrievedUser42.getPhone());
        assertEquals(User2.PASSWD_HASH, retrievedUser42.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, retrievedUser42.getPasswordSalt());
        assertEquals(User2.LOCALE, retrievedUser42.getLocale());
    }

    @Test
    public void testSaveNonExistentUserSingle() throws Exception
    {
        user1 = User1.getDomainObject();

        User[] users = { user1 };
        List<User> savedUsers = userDAO.saveUser(users);
        assertEquals(users.length, savedUsers.size());
        User savedUser1 = savedUsers.get(0);
        assertEquals(User1.NAME, savedUser1.getName());
        assertEquals(User1.EMAIL, savedUser1.getEmail());
        assertEquals(User1.PHONE, savedUser1.getPhone());
        assertEquals(User1.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User1.LOCALE, savedUser1.getLocale());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
    }

    @Test
    public void testSaveNonExistentUserMultiple() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();

        User[] users = { user1, user2 };
        List<User> savedUsers = userDAO.saveUser(users);
        assertEquals(users.length, savedUsers.size());
        User savedUser1 = null, savedUser2 = null;
        for (User user : savedUsers) {
            if (user.getEmail().equals(User1.EMAIL)) {
                savedUser1 = user;
            } else {
                savedUser2 = user;
            }
        }
        assertEquals(User1.NAME, savedUser1.getName());
        assertEquals(User1.EMAIL, savedUser1.getEmail());
        assertEquals(User1.PHONE, savedUser1.getPhone());
        assertEquals(User1.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User1.LOCALE, savedUser1.getLocale());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
        assertEquals(User2.NAME, savedUser2.getName());
        assertEquals(User2.EMAIL, savedUser2.getEmail());
        assertEquals(User2.PHONE, savedUser2.getPhone());
        assertEquals(User2.PASSWD_HASH, savedUser2.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, savedUser2.getPasswordSalt());
        assertEquals(User2.LOCALE, savedUser2.getLocale());
        assertNotNull(savedUser2.getId());
        assertTrue(savedUser2.getId() > 0);
    }

    @Test
    public void testSaveExistingUserSingle() throws Exception
    {
        user1 = User1.getDomainObject();
        TestDataHelper.createUser(user1);

        user1.setName(User2.NAME);
        String updateUser1Email = User1.EMAIL + ".ext";
        user1.setEmail(updateUser1Email);
        user1.setPhone(User2.PHONE);
        user1.setPasswordHash(User2.PASSWD_HASH);
        user1.setPasswordSalt(User2.PASSWD_SALT);
        user1.setLocale(User2.LOCALE);

        User[] users = { user1 };
        List<User> savedUsers = userDAO.saveUser(users);
        assertEquals(users.length, savedUsers.size());
        User savedUser1 = savedUsers.get(0);
        assertEquals(User2.NAME, savedUser1.getName());
        assertEquals(updateUser1Email, savedUser1.getEmail());
        assertEquals(User2.PHONE, savedUser1.getPhone());
        assertEquals(User2.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User2.LOCALE, savedUser1.getLocale());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
    }

    @Test
    public void testSaveExistingUserMultiple() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();
        TestDataHelper.createUser(user1);
        TestDataHelper.createUser(user2);

        user1.setName(User2.NAME);
        String updateUser1Email = User1.EMAIL + ".ext";
        user1.setEmail(updateUser1Email);
        user1.setPhone(User2.PHONE);
        user1.setPasswordHash(User2.PASSWD_HASH);
        user1.setPasswordSalt(User2.PASSWD_SALT);
        user1.setLocale(User2.LOCALE);

        user2.setName(User1.NAME);
        String updateUser2Email = User2.EMAIL + ".ext";
        user2.setEmail(updateUser2Email);
        user2.setPhone(User1.PHONE);
        user2.setPasswordHash(User1.PASSWD_HASH);
        user2.setPasswordSalt(User1.PASSWD_SALT);
        user2.setLocale(User1.LOCALE);

        User[] users = { user1, user2 };
        List<User> savedUsers = userDAO.saveUser(users);
        assertEquals(users.length, savedUsers.size());
        User savedUser1 = null, savedUser2 = null;
        for (User user : savedUsers) {
            if (user.getEmail().equals(updateUser1Email)) {
                savedUser1 = user;
            } else {
                savedUser2 = user;
            }
        }
        assertEquals(User2.NAME, savedUser1.getName());
        assertEquals(updateUser1Email, savedUser1.getEmail());
        assertEquals(User2.PHONE, savedUser1.getPhone());
        assertEquals(User2.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User2.LOCALE, savedUser1.getLocale());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
        assertEquals(User1.NAME, savedUser2.getName());
        assertEquals(updateUser2Email, savedUser2.getEmail());
        assertEquals(User1.PHONE, savedUser2.getPhone());
        assertEquals(User1.PASSWD_HASH, savedUser2.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, savedUser2.getPasswordSalt());
        assertEquals(User1.LOCALE, savedUser2.getLocale());
        assertNotNull(savedUser2.getId());
        assertTrue(savedUser2.getId() > 0);
    }

    /*
     * Insert one user and then perform a save with 2 users - one of which
     * should be an update while the other is an insert.
     */
    @Test
    public void testUpsertUser() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();
        TestDataHelper.createUser(user1);

        user1.setName(User2.NAME);
        String updateUser1Email = User1.EMAIL + ".ext";
        user1.setEmail(updateUser1Email);
        user1.setPhone(User2.PHONE);
        user1.setPasswordHash(User2.PASSWD_HASH);
        user1.setPasswordSalt(User2.PASSWD_SALT);
        user1.setLocale(User2.LOCALE);

        User[] users = { user1, user2 };
        List<User> savedUsers = userDAO.saveUser(users);
        assertEquals(users.length, savedUsers.size());
        User savedUser1 = null, savedUser2 = null;
        for (User user : savedUsers) {
            if (user.getEmail().equals(updateUser1Email)) {
                savedUser1 = user;
            } else {
                savedUser2 = user;
            }
        }
        assertEquals(User2.NAME, savedUser1.getName());
        assertEquals(updateUser1Email, savedUser1.getEmail());
        assertEquals(User2.PHONE, savedUser1.getPhone());
        assertEquals(User2.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User2.LOCALE, savedUser1.getLocale());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
        assertEquals(User2.NAME, savedUser2.getName());
        assertEquals(User2.EMAIL, savedUser2.getEmail());
        assertEquals(User2.PHONE, savedUser2.getPhone());
        assertEquals(User2.PASSWD_HASH, savedUser2.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, savedUser2.getPasswordSalt());
        assertEquals(User2.LOCALE, savedUser2.getLocale());
        assertNotNull(savedUser2.getId());
    }

    @Test
    public void testInactivateUserSingle() throws Exception
    {
        user1 = User1.getDomainObject();
        TestDataHelper.createUser(user1);
        user1.setActive(false);

        User[] users1 = { user1 };
        List<User> savedUsers1 = userDAO.saveUser(users1);
        assertEquals(users1.length, savedUsers1.size());
        User savedUser1 = savedUsers1.get(0);
        assertEquals(User1.NAME, savedUser1.getName());
        assertEquals(User1.EMAIL, savedUser1.getEmail());
        assertEquals(User1.PHONE, savedUser1.getPhone());
        assertEquals(User1.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User1.LOCALE, savedUser1.getLocale());
        assertEquals(false, savedUser1.getActive());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
    }

    @Test
    public void testInactivateUserMultiple() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();
        TestDataHelper.createUser(user1);
        TestDataHelper.createUser(user2);
        user1.setActive(false);
        user2.setActive(false);

        User[] users = { user1, user2 };
        List<User> savedUsers = userDAO.saveUser(users);
        assertEquals(users.length, savedUsers.size());
        User savedUser1 = null, savedUser2 = null;
        for (User user : savedUsers) {
            if (user.getEmail().equals(user1.getEmail())) {
                savedUser1 = user;
            } else {
                savedUser2 = user;
            }
        }
        assertEquals(User1.NAME, savedUser1.getName());
        assertEquals(User1.EMAIL, savedUser1.getEmail());
        assertEquals(User1.PHONE, savedUser1.getPhone());
        assertEquals(User1.PASSWD_HASH, savedUser1.getPasswordHash());
        assertEquals(User1.PASSWD_SALT, savedUser1.getPasswordSalt());
        assertEquals(User1.LOCALE, savedUser1.getLocale());
        assertEquals(false, savedUser1.getActive());
        assertNotNull(savedUser1.getId());
        assertTrue(savedUser1.getId() > 0);
        assertEquals(User2.NAME, savedUser2.getName());
        assertEquals(User2.EMAIL, savedUser2.getEmail());
        assertEquals(User2.PHONE, savedUser2.getPhone());
        assertEquals(User2.PASSWD_HASH, savedUser2.getPasswordHash());
        assertEquals(User2.PASSWD_SALT, savedUser2.getPasswordSalt());
        assertEquals(User2.LOCALE, savedUser2.getLocale());
        assertEquals(false, savedUser2.getActive());
        assertNotNull(savedUser2.getId());
        assertTrue(savedUser2.getId() > 0);
    }
}

