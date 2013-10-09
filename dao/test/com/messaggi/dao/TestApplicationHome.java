package com.messaggi.dao;

import static org.junit.Assert.fail;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.domain.Application;
import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.util.HibernateUtil;

public class TestApplicationHome extends MessaggiTestCase
{
    private static SessionFactory factory;

    private ApplicationHome dao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        factory = HibernateUtil.getSessionFactory();
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
        fail("Not yet implemented");
    }

    @Test
    public void testRemove()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testMerge()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testFindById()
    {
        long id1 = 123;
        Application a1 = dao.findById(id1);
    }
}

