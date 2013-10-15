package com.messaggi.persistence.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.persistence.domain.Application;
import com.messaggi.persistence.util.HibernateUtil;

public class TestApplicationHome extends MessaggiTestCase
{
    private ApplicationHome dao;

    public TestApplicationHome() throws NamingException
    {
        super();
        Context context = new InitialContext();
        context.bind("java:hibernate/SessionFactory", HibernateUtil.getSessionFactory());
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        dao = new ApplicationHome();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test
    public void testPersist()
    {
    }

    @Test
    public void testRemove()
    {
    }

    @Test
    public void testMerge()
    {
    }

    @Test
    public void testFindById()
    {
        long id1 = 123;
        Application a1 = dao.findById(id1);
    }
}

