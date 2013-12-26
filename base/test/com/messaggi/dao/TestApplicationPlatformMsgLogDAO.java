package com.messaggi.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Application1;
import com.messaggi.TestDataHelper.Application2;
import com.messaggi.TestDataHelper.Application3;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.ApplicationPlatformMsgLog;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestApplicationPlatformMsgLogDAO extends MessaggiTestCase
{
    private Application app1;

    private ApplicationPlatform appPlat1;

    private ApplicationPlatform appPlat2;

    private ApplicationPlatform appPlat3;

    private ApplicationPlatformMsgLog appPlatMsgLog1;

    private ApplicationPlatformMsgLog appPlatMsgLog2;

    private ApplicationPlatformMsgLog appPlatMsgLog3;

    private ApplicationPlatformMsgLog appPlatMsgLog4;

    private ApplicationPlatformMsgLog appPlatMsgLog5;

    private ApplicationPlatformMsgLog appPlatMsgLog6;

    private User user1;

    private ApplicationPlatformMsgLogDAO applicationPlatformMsgLogDAO;

    @Override
    @Before
    public void setUp() throws Exception
    {
        user1 = User1.getDomainObject();
        TestDataHelper.createUser(user1);
        app1 = Application1.getDomainObject();
        app1.setUser(user1);
        TestDataHelper.createApplication(app1);
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        TestDataHelper.createApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
        applicationPlatformMsgLogDAO = new ApplicationPlatformMsgLogDAO();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteApplicationPlatform(appPlat1);
        TestDataHelper.deleteApplicationPlatform(appPlat2);
        TestDataHelper.deleteApplicationPlatform(appPlat3);
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteUser(user1);
    }

    @Test
    public void testSaveNonExistentAppSingle() throws Exception
    {
        app1 = Application1.getDomainObject();
        app1.setUser(user1);

        ApplicationPlatformMsgLog[] msgLogs = { app1 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = savedApps.get(0);
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

        ApplicationPlatformMsgLog[] apps = { app1, app2, app3 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (ApplicationPlatformMsgLog app : savedApps) {
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

        ApplicationPlatformMsgLog[] apps = { app1 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = savedApps.get(0);
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

        ApplicationPlatformMsgLog[] apps = { app1, app2, app3 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (ApplicationPlatformMsgLog app : savedApps) {
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

        ApplicationPlatformMsgLog[] apps = { app1, app2, app3 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (ApplicationPlatformMsgLog app : savedApps) {
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

        ApplicationPlatformMsgLog[] apps = { app1 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = savedApps.get(0);
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

        ApplicationPlatformMsgLog[] apps = { app1, app2, app3 };
        List<ApplicationPlatformMsgLog> savedApps = applicationPlatformMsgLogDAO.saveApplicationPlatformMsgLog(apps);
        assertEquals(apps.length, savedApps.size());
        ApplicationPlatformMsgLog savedApp1 = null, savedApp2 = null, savedApp3 = null;
        for (ApplicationPlatformMsgLog app : savedApps) {
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

