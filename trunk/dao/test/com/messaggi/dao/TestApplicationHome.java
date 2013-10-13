package com.messaggi.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.dao.util.HibernateUtil;
import com.messaggi.domain.Application;
import com.messaggi.junit.MessaggiTestCase;

public class TestApplicationHome extends MessaggiTestCase
{
    private final SessionFactory factory;

    private ApplicationHome dao;

    public TestApplicationHome()
    {
        super();
        factory = HibernateUtil.getSessionFactory();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("messaggi");
        EntityManager em = emf.createEntityManager();
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

