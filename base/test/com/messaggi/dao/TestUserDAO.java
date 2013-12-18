package com.messaggi.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestUserDAO extends MessaggiTestCase
{
    private UserDAO userDAO;

    @Override
    @Before
    public void setUp() throws Exception
    {
        TestDataHelper.deleteUser1();
        TestDataHelper.deleteUser2();
        userDAO = new UserDAO();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteUser1();
        TestDataHelper.deleteUser2();
    }

    @Test
    public void testGetUser() throws Exception
    {
        User[] users = { User1.getDomainObject() };
        userDAO.getUser(users);
    }

    @Test
    public void testSaveUser() throws Exception
    {
        User[] users = { User1.getDomainObject() };
        userDAO.saveUser(users);
    }
}

