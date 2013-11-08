package com.messaggi.persistence.dao.postgresql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.DAOException.ErrorCode;
import com.messaggi.persistence.dao.DAOFactory;
import com.messaggi.persistence.dao.DAOHelper;
import com.messaggi.persistence.dao.UserDAO;
import com.messaggi.persistence.domain.DomainTestHelper;
import com.messaggi.persistence.domain.User;

public class TestPostgreSQLUserDAO
{
    private UserDAO dao;

    private User user1;

    private User user2;

    @Before
    public void setUp() throws Exception
    {
        dao = DAOFactory.getDAOFactory(DAOFactory.Factory.PostgreSQL).getUserDAO();
        user1 = DomainTestHelper.generateRandomUser(Locale.US);
        user2 = DomainTestHelper.generateRandomUser(Locale.ITALY);
    }

    @After
    public void tearDown() throws Exception
    {
        DAOTestHelper.deleteTestUser(user1.getId(), user2.getId());
    }

    @Test
    public void testInsertUserOneVersion() throws Exception
    {
        List<User> users = new ArrayList<>();
        users.add(user1);
        List<User> insertedUsers = dao.insertUser(users);
        assertEquals(1, insertedUsers.size());
        assertNotNull(insertedUsers.get(0).getId());
        assertNotNull(insertedUsers.get(0).getPhoneParsed());
        assertTrue(insertedUsers.get(0).getActive());
        // Set this on the prototype so we can remove the test user in tearDown.
        user1.setId(insertedUsers.get(0).getId());
    }

    @Test
    public void testInsertUserMultipleVersion() throws Exception
    {
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<User> insertedUsers = dao.insertUser(users);
        assertEquals(2, insertedUsers.size());
        assertNotNull(insertedUsers.get(0).getId());
        assertNotNull(insertedUsers.get(0).getPhoneParsed());
        assertTrue(insertedUsers.get(0).getActive());
        assertNotNull(insertedUsers.get(1).getId());
        assertNotNull(insertedUsers.get(1).getPhoneParsed());
        assertTrue(insertedUsers.get(1).getActive());
        // Set this on the prototype so we can remove the test user in tearDown.
        user1.setId(insertedUsers.get(0).getId());
        user2.setId(insertedUsers.get(1).getId());
    }

    @Test
    public void testInsertUserExceptions() throws Exception
    {
        List<User> users1 = new ArrayList<>();
        users1.add(user1);
        users1.add(user2);
        users1.add(user1);
        try {
            dao.insertUser(users1);
            fail("Should have thrown dao exception");
        } catch (DAOException e) {
            assertEquals(ErrorCode.FAIL_TO_INSERT, e.getErrorCode());
            assertTrue(e.getMessage().contains("duplicate key value violates unique constraint \"user-email-key\""));
        }

        List<User> users2 = new ArrayList<>();
        // Null out the user name which is a required field.
        user1.setName(null);
        users2.add(user1);
        try {
            dao.insertUser(users2);
            fail("Should have thrown dao exception");
        } catch (DAOException e) {
            assertEquals(ErrorCode.FAIL_TO_INSERT, e.getErrorCode());
            assertTrue(e.getMessage().contains("null value in column \"name\" violates not-null constraint"));
        }
    }

    @Test
    public void testSelectUserOneVersion() throws Exception
    {
        User user1Clone = DAOHelper.clonePrototype(user1);
        DAOTestHelper.insertTestUser(user1);

        // Select by email.
        List<User> users1 = new ArrayList<>();
        users1.add(user1Clone);
        List<User> selectedUsers1 = dao.selectUser(users1);
        assertEquals(1, selectedUsers1.size());
        assertNotNull(selectedUsers1.get(0).getId());
        assertEquals(user1.getId(), selectedUsers1.get(0).getId());

        // Select by id.
        user1Clone.setEmail(null);
        user1Clone.setId(user1.getId());
        List<User> selectedUsers2 = dao.selectUser(users1);
        assertEquals(1, selectedUsers2.size());
        assertNotNull(selectedUsers2.get(0).getId());
        assertEquals(user1.getId(), selectedUsers2.get(0).getId());
    }

    @Test
    public void testSelectUserMultipleVersion() throws Exception
    {
        User user1Clone = DAOHelper.clonePrototype(user1);
        User user2Clone = DAOHelper.clonePrototype(user2);
        DAOTestHelper.insertTestUser(user1, user2);
        
        // Select by email.
        List<User> users1 = new ArrayList<>();
        users1.add(user1Clone);
        users1.add(user2Clone);
        List<User> selectedUsers1 = dao.selectUser(users1);
        assertEquals(2, selectedUsers1.size());
        assertNotNull(selectedUsers1.get(0).getId());
        assertNotNull(selectedUsers1.get(1).getId());
        assertEquals(user1.getId(), selectedUsers1.get(0).getId());
        assertEquals(user2.getId(), selectedUsers1.get(1).getId());

        // Select by id.
        user1Clone.setEmail(null);
        user1Clone.setId(user1.getId());
        user2Clone.setEmail(null);
        user2Clone.setId(user2.getId());
        List<User> selectedUsers2 = dao.selectUser(users1);
        assertEquals(2, selectedUsers2.size());
        assertNotNull(selectedUsers2.get(0).getId());
        assertNotNull(selectedUsers2.get(1).getId());
        assertEquals(user1.getId(), selectedUsers2.get(0).getId());
        assertEquals(user2.getId(), selectedUsers2.get(1).getId());
    }

    @Test
    public void testSelectUserExceptions() throws Exception
    {
        User user1Clone = DAOHelper.clonePrototype(user1);
        DAOTestHelper.insertTestUser(user1);
        
        List<User> users1 = new ArrayList<>();
        // Set id in addition to email so we will have 2 select criteria.
        user1Clone.setId(user1.getId());
        users1.add(user1Clone);
        try {
            dao.selectUser(users1);
            fail("Should have thrown dao exception");
        } catch (DAOException e) {
            assertEquals(ErrorCode.INVALID_CRITERIA, e.getErrorCode());
            assertEquals(UserDAO.Messages.BOTH_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE, e.getMessage());
        }

        List<User> users2 = new ArrayList<>();
        // Null out id and email so we won't have valid select criteria (id or email).
        user1Clone.setEmail(null);
        user1Clone.setId(null);
        users2.add(user1Clone);
        try {
            dao.selectUser(users2);
            fail("Should have thrown dao exception");
        } catch (DAOException e) {
            assertEquals(ErrorCode.INVALID_CRITERIA, e.getErrorCode());
            assertEquals(UserDAO.Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void testSelectUserNoResult() throws Exception
    {
        User user1Clone = DAOHelper.clonePrototype(user1);
        DAOTestHelper.insertTestUser(user1);

        // Select by email.
        List<User> users1 = new ArrayList<>();
        user1Clone.setEmail("something_that_does_not_match@yahoo.com");
        users1.add(user1Clone);
        List<User> selectedUsers1 = dao.selectUser(users1);
        assertEquals(0, selectedUsers1.size());

        // Select by id.
        user1Clone.setEmail(null);
        user1Clone.setId(123456789L);
        List<User> selectedUsers2 = dao.selectUser(users1);
        assertEquals(0, selectedUsers2.size());
    }

    @Test
    public void testSelectUserInactiveUser() throws Exception
    {
        User user1Clone = DAOHelper.clonePrototype(user1);
        user1.setActive(false);
        DAOTestHelper.insertTestUser(user1);

        // Select by email.
        List<User> users1 = new ArrayList<>();
        users1.add(user1Clone);
        List<User> selectedUsers1 = dao.selectUser(users1);
        assertEquals(0, selectedUsers1.size());

        // Select by id.
        user1Clone.setEmail(null);
        user1Clone.setId(user1.getId());
        List<User> selectedUsers2 = dao.selectUser(users1);
        assertEquals(0, selectedUsers2.size());
    }

    @Test
    public void testUpdateUserOneVersion() throws Exception
    {
        DAOTestHelper.insertTestUser(user1);
        
        String newName1 = "new name";
        String newEmail1 = "new_name@yahoo.com";
        String newPhone1 = "617-123-2222";
        String newPassword1 = "new password1";
        Locale newLocale1 = Locale.CANADA;
        boolean newActive1 = false;
        
        // Select by email.
        List<User> users1 = new ArrayList<>();
        user1.setName(newName1);
        user1.setEmail(newEmail1);
        user1.setPhone(newPhone1);
        user1.setPassword(newPassword1);
        user1.setLocale(newLocale1);
        user1.setActive(newActive1);
        users1.add(user1);
        dao.updateUser(users1);
        List<User> usersPostUpdate = DAOTestHelper.selectTestUser(user1);
        assertEquals(user1.getId(), usersPostUpdate.get(0).getId());
        assertEquals(user1.getName(), usersPostUpdate.get(0).getName());
        assertEquals(user1.getEmail(), usersPostUpdate.get(0).getEmail());
        assertEquals(user1.getPhone(), usersPostUpdate.get(0).getPhone());
        assertEquals(user1.getPassword(), usersPostUpdate.get(0).getPassword());
        assertEquals(user1.getLocale(), usersPostUpdate.get(0).getLocale());
        assertEquals(user1.getActive(), usersPostUpdate.get(0).getActive());
    }

    @Test
    public void testUpdateUserMultipleVersion() throws Exception
    {
        DAOTestHelper.insertTestUser(user1, user2);

        String newName1 = "new name1";
        String newName2 = "new name2";
        String newEmail1 = "new_name1@yahoo.com";
        String newEmail2 = "new_name2@yahoo.com";
        String newPhone1 = "617-123-2222";
        String newPhone2 = "617-123-3333";
        String newPassword1 = "new password1";
        String newPassword2 = "new password2";
        Locale newLocale1 = Locale.CANADA;
        Locale newLocale2 = Locale.GERMAN;
        boolean newActive1 = false;
        boolean newActive2 = false;

        // Select by email.
        List<User> users1 = new ArrayList<>();
        user1.setName(newName1);
        user2.setName(newName2);
        user1.setEmail(newEmail1);
        user2.setEmail(newEmail2);
        user1.setPhone(newPhone1);
        user2.setPhone(newPhone2);
        user1.setPassword(newPassword1);
        user2.setPassword(newPassword2);
        user1.setLocale(newLocale1);
        user2.setLocale(newLocale2);
        user1.setActive(newActive1);
        user2.setActive(newActive2);
        users1.add(user1);
        users1.add(user2);
        dao.updateUser(users1);
        List<User> usersPostUpdate = DAOTestHelper.selectTestUser(user1, user2);
        assertEquals(user1.getId(), usersPostUpdate.get(0).getId());
        assertEquals(user2.getId(), usersPostUpdate.get(1).getId());
        assertEquals(user1.getName(), usersPostUpdate.get(0).getName());
        assertEquals(user2.getName(), usersPostUpdate.get(1).getName());
        assertEquals(user1.getEmail(), usersPostUpdate.get(0).getEmail());
        assertEquals(user2.getEmail(), usersPostUpdate.get(1).getEmail());
        assertEquals(user1.getPhone(), usersPostUpdate.get(0).getPhone());
        assertEquals(user2.getPhone(), usersPostUpdate.get(1).getPhone());
        assertEquals(user1.getPassword(), usersPostUpdate.get(0).getPassword());
        assertEquals(user2.getPassword(), usersPostUpdate.get(1).getPassword());
        assertEquals(user1.getLocale(), usersPostUpdate.get(0).getLocale());
        assertEquals(user2.getLocale(), usersPostUpdate.get(1).getLocale());
        assertEquals(user1.getActive(), usersPostUpdate.get(0).getActive());
        assertEquals(user2.getActive(), usersPostUpdate.get(1).getActive());
    }

    @Test
    public void testDeleteUserOneVersion() throws Exception
    {
        DAOTestHelper.insertTestUser(user1);
        List<User> usersPreDelete = DAOTestHelper.selectTestUser(user1);
        assertEquals(1, usersPreDelete.size());
        assertEquals(true, usersPreDelete.get(0).getActive());

        List<User> users1 = new ArrayList<>();
        users1.add(user1);
        dao.deleteUser(users1);
        List<User> usersPostDelete = DAOTestHelper.selectTestUser(user1);
        assertEquals(1, usersPostDelete.size());
        assertEquals(false, usersPostDelete.get(0).getActive());
    }

    @Test
    public void testDeleteUserOneVersionMultipleTimes() throws Exception
    {
        DAOTestHelper.insertTestUser(user1);
        List<User> usersPreDelete = DAOTestHelper.selectTestUser(user1);
        assertEquals(1, usersPreDelete.size());
        assertEquals(true, usersPreDelete.get(0).getActive());

        List<User> users1 = new ArrayList<>();
        users1.add(user1);
        users1.add(user1);
        dao.deleteUser(users1);
        List<User> usersPostDelete = DAOTestHelper.selectTestUser(user1);
        assertEquals(1, usersPostDelete.size());
        assertEquals(false, usersPostDelete.get(0).getActive());
    }

    @Test
    public void testDeleteUserMultipleVersion() throws Exception
    {
        DAOTestHelper.insertTestUser(user1, user2);
        List<User> usersPreDelete = DAOTestHelper.selectTestUser(user1, user2);
        assertEquals(2, usersPreDelete.size());
        assertEquals(true, usersPreDelete.get(0).getActive());
        assertEquals(true, usersPreDelete.get(1).getActive());

        List<User> users1 = new ArrayList<>();
        users1.add(user1);
        users1.add(user2);
        dao.deleteUser(users1);
        List<User> usersPostDelete = DAOTestHelper.selectTestUser(user1, user2);
        assertEquals(2, usersPostDelete.size());
        assertEquals(false, usersPostDelete.get(0).getActive());
        assertEquals(false, usersPostDelete.get(1).getActive());
    }
}

