package com.messaggi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Application1;
import com.messaggi.TestDataHelper.Application2;
import com.messaggi.TestDataHelper.Application3;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.TestDataHelper.User2;
import com.messaggi.domain.Application;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestApplicationDAO extends MessaggiTestCase
{
    private Application app1;

    private Application app2;

    private Application app3;

    private User user1;

    private User user2;

    private ApplicationDAO applicationDAO;

    @Override
    @Before
    public void setUp() throws Exception
    {
        user1 = User1.getDomainObject();
        user2 = User2.getDomainObject();
        TestDataHelper.createUser(user1);
        TestDataHelper.createUser(user2);
        applicationDAO = new ApplicationDAO();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteApplication(app2);
        TestDataHelper.deleteApplication(app3);
        TestDataHelper.deleteUser(user1);
        TestDataHelper.deleteUser(user2);
    }
    
    @Test
    public void testSaveNonExistentAppSingle() throws Exception
    {
        app1 = Application1.getDomainObject();
        app1.setUser(user1);

        Application[] apps = { app1 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = savedApps.get(0);
        app1.setId(savedApp1.getId());
        assertEquals(Application1.NAME, savedApp1.getName());
        assertEquals(user1.getId(), savedApp1.getUser().getId());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
    }

    @Test
    public void testSaveNonExistentAppMultiple() throws Exception
    {
        app1 = Application1.getDomainObject();
        app2 = Application2.getDomainObject();
        app3 = Application3.getDomainObject();
        app1.setUser(user1);
        app2.setUser(user1);
        app3.setUser(user2);

        Application[] apps = { app1, app2, app3 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (Application app : savedApps) {
            if (app.getName().equals(Application1.NAME)) {
                savedApp1 = app;
            } else if (app.getName().equals(Application2.NAME)) {
                savedApp2 = app;
            } else {
                savedApp3 = app;
            }
        }
        app1.setId(savedApp1.getId());
        app2.setId(savedApp2.getId());
        app3.setId(savedApp3.getId());
        assertEquals(Application1.NAME, savedApp1.getName());
        assertEquals(user1.getId(), savedApp1.getUser().getId());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
        assertEquals(Application2.NAME, savedApp2.getName());
        assertEquals(user1.getId(), savedApp2.getUser().getId());
        assertNotNull(savedApp2.getId());
        assertTrue(savedApp2.getId() > 0);
        assertEquals(Application3.NAME, savedApp3.getName());
        assertEquals(user2.getId(), savedApp3.getUser().getId());
        assertNotNull(savedApp3.getId());
        assertTrue(savedApp3.getId() > 0);
    }

    @Test
    public void testSaveExistingAppSingle() throws Exception
    {
        app1 = Application1.getDomainObject();
        app1.setUser(user1);
        TestDataHelper.createApplication(app1);

        String updateApp1Name = Application1.NAME + "-2.0";
        app1.setName(updateApp1Name);
        app1.setUser(user2);

        Application[] apps = { app1 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = savedApps.get(0);
        assertEquals(updateApp1Name, savedApp1.getName());
        assertEquals(user2.getId(), savedApp1.getUser().getId());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
    }

    @Test
    public void testSaveExistingAppMultiple() throws Exception
    {
        app1 = Application1.getDomainObject();
        app2 = Application2.getDomainObject();
        app3 = Application3.getDomainObject();
        app1.setUser(user1);
        app2.setUser(user1);
        app3.setUser(user2);
        TestDataHelper.createApplication(app1);
        TestDataHelper.createApplication(app2);
        TestDataHelper.createApplication(app3);

        String updateApp1Name = Application1.NAME + "-2.0";
        app1.setName(updateApp1Name);
        app1.setUser(user2);

        String updateApp2Name = Application2.NAME + "-2.1";
        app2.setName(updateApp2Name);
        app2.setUser(user2);

        String updateApp3Name = Application3.NAME + "-2.2";
        app3.setName(updateApp3Name);
        app3.setUser(user1);

        Application[] apps = { app1, app2, app3 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (Application app : savedApps) {
            if (app.getName().equals(updateApp1Name)) {
                savedApp1 = app;
            } else if (app.getName().equals(updateApp2Name)) {
                savedApp2 = app;
            } else {
                savedApp3 = app;
            }
        }
        assertEquals(apps.length, savedApps.size());
        assertEquals(updateApp1Name, savedApp1.getName());
        assertEquals(user2.getId(), savedApp1.getUser().getId());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
        assertEquals(updateApp2Name, savedApp2.getName());
        assertEquals(user2.getId(), savedApp2.getUser().getId());
        assertNotNull(savedApp2.getId());
        assertTrue(savedApp2.getId() > 0);
        assertEquals(updateApp3Name, savedApp3.getName());
        assertEquals(user1.getId(), savedApp3.getUser().getId());
        assertNotNull(savedApp3.getId());
        assertTrue(savedApp3.getId() > 0);
    }

    /*
     * Insert one application and then perform a save with 3 applications - one
     * of which should be an update while the other 2 are inserts.
     */
    @Test
    public void testUpsertApp() throws Exception
    {
        app1 = Application1.getDomainObject();
        app2 = Application2.getDomainObject();
        app3 = Application3.getDomainObject();
        app1.setUser(user1);
        app2.setUser(user1);
        app3.setUser(user2);
        TestDataHelper.createApplication(app1);

        String updateApp1Name = Application1.NAME + "-2.0";
        app1.setName(updateApp1Name);
        app1.setUser(user2);

        Application[] apps = { app1, app2, app3 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (Application app : savedApps) {
            if (app.getName().equals(updateApp1Name)) {
                savedApp1 = app;
            } else if (app.getName().equals(Application2.NAME)) {
                savedApp2 = app;
            } else {
                savedApp3 = app;
            }
        }
        app2.setId(savedApp2.getId());
        app3.setId(savedApp3.getId());
        assertEquals(updateApp1Name, savedApp1.getName());
        assertEquals(user2.getId(), savedApp1.getUser().getId());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
        assertEquals(Application2.NAME, savedApp2.getName());
        assertEquals(user1.getId(), savedApp2.getUser().getId());
        assertNotNull(savedApp2.getId());
        assertTrue(savedApp2.getId() > 0);
        assertEquals(Application3.NAME, savedApp3.getName());
        assertEquals(user2.getId(), savedApp3.getUser().getId());
        assertNotNull(savedApp3.getId());
        assertTrue(savedApp3.getId() > 0);
    }

    @Test
    public void testInactivateAppSingle() throws Exception
    {
        app1 = Application1.getDomainObject();
        app1.setUser(user1);
        TestDataHelper.createApplication(app1);
        app1.setActive(false);

        Application[] apps = { app1 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = savedApps.get(0);
        assertEquals(Application1.NAME, savedApp1.getName());
        assertEquals(user1.getId(), savedApp1.getUser().getId());
        assertEquals(false, savedApp1.getActive());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
    }

    @Test
    public void testInactivateAppMultiple() throws Exception
    {
        app1 = Application1.getDomainObject();
        app2 = Application2.getDomainObject();
        app3 = Application3.getDomainObject();
        app1.setUser(user1);
        app2.setUser(user1);
        app3.setUser(user2);
        TestDataHelper.createApplication(app1);
        TestDataHelper.createApplication(app2);
        TestDataHelper.createApplication(app3);
        app1.setActive(false);
        app2.setActive(false);
        app3.setActive(false);

        Application[] apps = { app1, app2, app3 };
        List<Application> savedApps = applicationDAO.saveApplication(apps);
        assertEquals(apps.length, savedApps.size());
        Application savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (Application app : savedApps) {
            if (app.getName().equals(Application1.NAME)) {
                savedApp1 = app;
            } else if (app.getName().equals(Application2.NAME)) {
                savedApp2 = app;
            } else {
                savedApp3 = app;
            }
        }
        assertEquals(Application1.NAME, savedApp1.getName());
        assertEquals(user1.getId(), savedApp1.getUser().getId());
        assertEquals(false, savedApp1.getActive());
        assertNotNull(savedApp1.getId());
        assertTrue(savedApp1.getId() > 0);
        assertEquals(user1.getId(), savedApp2.getUser().getId());
        assertEquals(false, savedApp2.getActive());
        assertNotNull(savedApp2.getId());
        assertTrue(savedApp2.getId() > 0);
        assertEquals(user2.getId(), savedApp3.getUser().getId());
        assertEquals(false, savedApp3.getActive());
        assertNotNull(savedApp3.getId());
        assertTrue(savedApp3.getId() > 0);
    }
}

