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
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.ApplicationPlatformMsgLog1;
import com.messaggi.TestDataHelper.ApplicationPlatformMsgLog2;
import com.messaggi.TestDataHelper.ApplicationPlatformMsgLog3;
import com.messaggi.TestDataHelper.ApplicationPlatformMsgLog4;
import com.messaggi.TestDataHelper.ApplicationPlatformMsgLog5;
import com.messaggi.TestDataHelper.ApplicationPlatformMsgLog6;
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
        TestDataHelper.deleteApplicationPlatformMsgLog(appPlatMsgLog1);
        TestDataHelper.deleteApplicationPlatformMsgLog(appPlatMsgLog2);
        TestDataHelper.deleteApplicationPlatformMsgLog(appPlatMsgLog3);
        TestDataHelper.deleteApplicationPlatformMsgLog(appPlatMsgLog4);
        TestDataHelper.deleteApplicationPlatformMsgLog(appPlatMsgLog5);
        TestDataHelper.deleteApplicationPlatformMsgLog(appPlatMsgLog6);
        TestDataHelper.deleteApplicationPlatform(appPlat1);
        TestDataHelper.deleteApplicationPlatform(appPlat2);
        TestDataHelper.deleteApplicationPlatform(appPlat3);
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteUser(user1);
    }

    @Test
    public void testSaveNonExistentAppSingle() throws Exception
    {
        appPlatMsgLog1 = ApplicationPlatformMsgLog1.getDomainObject();
        appPlatMsgLog1.setApplicationPlatform(appPlat1);

        ApplicationPlatformMsgLog[] msgLogs = { appPlatMsgLog1 };
        List<ApplicationPlatformMsgLog> savedAppPlatMsgLogs = applicationPlatformMsgLogDAO
                .saveApplicationPlatformMsgLog(msgLogs);
        assertEquals(msgLogs.length, savedAppPlatMsgLogs.size());
        ApplicationPlatformMsgLog savedAppPlatMsgLog1 = savedAppPlatMsgLogs.get(0);
        appPlatMsgLog1.setId(savedAppPlatMsgLog1.getId());
        assertEquals(appPlat1.getId(), savedAppPlatMsgLog1.getApplicationPlatform().getId());
        assertEquals(1, savedAppPlatMsgLog1.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog1.DATE, savedAppPlatMsgLog1.getDate());
        assertEquals(ApplicationPlatformMsgLog1.MSG_COUNT, savedAppPlatMsgLog1.getMsgCount());
        assertNotNull(savedAppPlatMsgLog1.getId());
        assertTrue(savedAppPlatMsgLog1.getId() > 0);
    }

    @Test
    public void testSaveNonExistentAppMultiple() throws Exception
    {
        appPlatMsgLog1 = ApplicationPlatformMsgLog1.getDomainObject();
        appPlatMsgLog2 = ApplicationPlatformMsgLog2.getDomainObject();
        appPlatMsgLog3 = ApplicationPlatformMsgLog3.getDomainObject();
        appPlatMsgLog4 = ApplicationPlatformMsgLog4.getDomainObject();
        appPlatMsgLog5 = ApplicationPlatformMsgLog5.getDomainObject();
        appPlatMsgLog6 = ApplicationPlatformMsgLog6.getDomainObject();
        appPlatMsgLog1.setApplicationPlatform(appPlat1);
        appPlatMsgLog2.setApplicationPlatform(appPlat1);
        appPlatMsgLog3.setApplicationPlatform(appPlat2);
        appPlatMsgLog4.setApplicationPlatform(appPlat2);
        appPlatMsgLog5.setApplicationPlatform(appPlat3);
        appPlatMsgLog6.setApplicationPlatform(appPlat3);

        ApplicationPlatformMsgLog[] msgLogs = { appPlatMsgLog1, appPlatMsgLog2, appPlatMsgLog3, appPlatMsgLog4,
                appPlatMsgLog5, appPlatMsgLog6 };
        List<ApplicationPlatformMsgLog> savedAppPlatMsgLogs = applicationPlatformMsgLogDAO
                .saveApplicationPlatformMsgLog(msgLogs);
        assertEquals(msgLogs.length, savedAppPlatMsgLogs.size());
        ApplicationPlatformMsgLog savedAppPlatMsgLog1 = null, savedAppPlatMsgLog2 = null, savedAppPlatMsgLog3 = null, savedAppPlatMsgLog4 = null, savedAppPlatMsgLog5 = null, savedAppPlatMsgLog6 = null;
        for (ApplicationPlatformMsgLog appPlatMsgLog : savedAppPlatMsgLogs) {
            if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog1.MSG_COUNT)) {
                savedAppPlatMsgLog1 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog2.MSG_COUNT)) {
                savedAppPlatMsgLog2 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog3.MSG_COUNT)) {
                savedAppPlatMsgLog3 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog4.MSG_COUNT)) {
                savedAppPlatMsgLog4 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog5.MSG_COUNT)) {
                savedAppPlatMsgLog5 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog6.MSG_COUNT)) {
                savedAppPlatMsgLog6 = appPlatMsgLog;
            }
        }
        appPlatMsgLog1.setId(savedAppPlatMsgLog1.getId());
        appPlatMsgLog2.setId(savedAppPlatMsgLog2.getId());
        appPlatMsgLog3.setId(savedAppPlatMsgLog3.getId());
        appPlatMsgLog4.setId(savedAppPlatMsgLog4.getId());
        appPlatMsgLog5.setId(savedAppPlatMsgLog5.getId());
        appPlatMsgLog6.setId(savedAppPlatMsgLog6.getId());
        assertEquals(appPlat1.getId(), savedAppPlatMsgLog1.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog1.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog1.DATE, savedAppPlatMsgLog1.getDate());
        assertEquals(ApplicationPlatformMsgLog1.MSG_COUNT, savedAppPlatMsgLog1.getMsgCount());
        assertNotNull(savedAppPlatMsgLog1.getId());
        assertTrue(savedAppPlatMsgLog1.getId() > 0);
        assertEquals(appPlat1.getId(), savedAppPlatMsgLog2.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog2.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog2.DATE, savedAppPlatMsgLog2.getDate());
        assertEquals(ApplicationPlatformMsgLog2.MSG_COUNT, savedAppPlatMsgLog2.getMsgCount());
        assertNotNull(savedAppPlatMsgLog2.getId());
        assertTrue(savedAppPlatMsgLog2.getId() > 0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog3.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog3.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog3.DATE, savedAppPlatMsgLog3.getDate());
        assertEquals(ApplicationPlatformMsgLog3.MSG_COUNT, savedAppPlatMsgLog3.getMsgCount());
        assertNotNull(savedAppPlatMsgLog3.getId());
        assertTrue(savedAppPlatMsgLog3.getId() > 0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog4.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog4.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog4.DATE, savedAppPlatMsgLog4.getDate());
        assertEquals(ApplicationPlatformMsgLog4.MSG_COUNT, savedAppPlatMsgLog4.getMsgCount());
        assertNotNull(savedAppPlatMsgLog4.getId());
        assertTrue(savedAppPlatMsgLog4.getId() > 0);
        assertEquals(appPlat3.getId(), savedAppPlatMsgLog5.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog5.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog5.DATE, savedAppPlatMsgLog5.getDate());
        assertEquals(ApplicationPlatformMsgLog5.MSG_COUNT, savedAppPlatMsgLog5.getMsgCount());
        assertNotNull(savedAppPlatMsgLog5.getId());
        assertTrue(savedAppPlatMsgLog5.getId() > 0);
        assertEquals(appPlat3.getId(), savedAppPlatMsgLog6.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog6.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog6.DATE, savedAppPlatMsgLog6.getDate());
        assertEquals(ApplicationPlatformMsgLog6.MSG_COUNT, savedAppPlatMsgLog6.getMsgCount());
        assertNotNull(savedAppPlatMsgLog6.getId());
        assertTrue(savedAppPlatMsgLog6.getId() > 0);
    }

    @Test
    public void testSaveExistingAppSingle() throws Exception
    {
        appPlatMsgLog1 = ApplicationPlatformMsgLog1.getDomainObject();
        appPlatMsgLog1.setApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog1);

        appPlatMsgLog1.setApplicationPlatform(appPlat2);
        appPlatMsgLog1.setDate(ApplicationPlatformMsgLog2.DATE);
        appPlatMsgLog1.setMsgCount(ApplicationPlatformMsgLog2.MSG_COUNT);

        ApplicationPlatformMsgLog[] msgLogs = { appPlatMsgLog1 };
        List<ApplicationPlatformMsgLog> savedAppPlatMsgLogs = applicationPlatformMsgLogDAO
                .saveApplicationPlatformMsgLog(msgLogs);
        assertEquals(msgLogs.length, savedAppPlatMsgLogs.size());
        ApplicationPlatformMsgLog savedAppPlatMsgLog1 = savedAppPlatMsgLogs.get(0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog1.getApplicationPlatform().getId());
        assertEquals(1, savedAppPlatMsgLog1.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog2.DATE, savedAppPlatMsgLog1.getDate());
        assertEquals(ApplicationPlatformMsgLog2.MSG_COUNT, savedAppPlatMsgLog1.getMsgCount());
        assertNotNull(savedAppPlatMsgLog1.getId());
        assertTrue(savedAppPlatMsgLog1.getId() > 0);
    }

    @Test
    public void testSaveExistingAppMultiple() throws Exception
    {
        appPlatMsgLog1 = ApplicationPlatformMsgLog1.getDomainObject();
        appPlatMsgLog2 = ApplicationPlatformMsgLog2.getDomainObject();
        appPlatMsgLog3 = ApplicationPlatformMsgLog3.getDomainObject();
        appPlatMsgLog4 = ApplicationPlatformMsgLog4.getDomainObject();
        appPlatMsgLog5 = ApplicationPlatformMsgLog5.getDomainObject();
        appPlatMsgLog6 = ApplicationPlatformMsgLog6.getDomainObject();
        appPlatMsgLog1.setApplicationPlatform(appPlat1);
        appPlatMsgLog2.setApplicationPlatform(appPlat1);
        appPlatMsgLog3.setApplicationPlatform(appPlat2);
        appPlatMsgLog4.setApplicationPlatform(appPlat2);
        appPlatMsgLog5.setApplicationPlatform(appPlat3);
        appPlatMsgLog6.setApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog1);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog2);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog3);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog4);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog5);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog6);

        appPlatMsgLog1.setApplicationPlatform(appPlat2);
        appPlatMsgLog1.setDate(ApplicationPlatformMsgLog2.DATE);
        appPlatMsgLog1.setMsgCount(ApplicationPlatformMsgLog2.MSG_COUNT);

        appPlatMsgLog2.setApplicationPlatform(appPlat2);
        appPlatMsgLog2.setDate(ApplicationPlatformMsgLog1.DATE);
        appPlatMsgLog2.setMsgCount(ApplicationPlatformMsgLog1.MSG_COUNT);

        appPlatMsgLog3.setApplicationPlatform(appPlat3);
        appPlatMsgLog3.setDate(ApplicationPlatformMsgLog4.DATE);
        appPlatMsgLog3.setMsgCount(ApplicationPlatformMsgLog4.MSG_COUNT);

        appPlatMsgLog4.setApplicationPlatform(appPlat3);
        appPlatMsgLog4.setDate(ApplicationPlatformMsgLog3.DATE);
        appPlatMsgLog4.setMsgCount(ApplicationPlatformMsgLog3.MSG_COUNT);

        appPlatMsgLog5.setApplicationPlatform(appPlat1);
        appPlatMsgLog5.setDate(ApplicationPlatformMsgLog6.DATE);
        appPlatMsgLog5.setMsgCount(ApplicationPlatformMsgLog6.MSG_COUNT);

        appPlatMsgLog6.setApplicationPlatform(appPlat1);
        appPlatMsgLog6.setDate(ApplicationPlatformMsgLog5.DATE);
        appPlatMsgLog6.setMsgCount(ApplicationPlatformMsgLog5.MSG_COUNT);

        ApplicationPlatformMsgLog[] msgLogs = { appPlatMsgLog1, appPlatMsgLog2, appPlatMsgLog3, appPlatMsgLog4,
                appPlatMsgLog5, appPlatMsgLog6 };
        List<ApplicationPlatformMsgLog> savedAppPlatMsgLogs = applicationPlatformMsgLogDAO
                .saveApplicationPlatformMsgLog(msgLogs);
        assertEquals(msgLogs.length, savedAppPlatMsgLogs.size());
        ApplicationPlatformMsgLog savedAppPlatMsgLog1 = null, savedAppPlatMsgLog2 = null, savedAppPlatMsgLog3 = null, savedAppPlatMsgLog4 = null, savedAppPlatMsgLog5 = null, savedAppPlatMsgLog6 = null;
        for (ApplicationPlatformMsgLog appPlatMsgLog : savedAppPlatMsgLogs) {
            if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog2.MSG_COUNT)) {
                savedAppPlatMsgLog1 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog1.MSG_COUNT)) {
                savedAppPlatMsgLog2 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog4.MSG_COUNT)) {
                savedAppPlatMsgLog3 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog3.MSG_COUNT)) {
                savedAppPlatMsgLog4 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog6.MSG_COUNT)) {
                savedAppPlatMsgLog5 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog5.MSG_COUNT)) {
                savedAppPlatMsgLog6 = appPlatMsgLog;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog1.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog1.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog2.DATE, savedAppPlatMsgLog1.getDate());
        assertEquals(ApplicationPlatformMsgLog2.MSG_COUNT, savedAppPlatMsgLog1.getMsgCount());
        assertNotNull(savedAppPlatMsgLog1.getId());
        assertTrue(savedAppPlatMsgLog1.getId() > 0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog2.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog2.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog1.DATE, savedAppPlatMsgLog2.getDate());
        assertEquals(ApplicationPlatformMsgLog1.MSG_COUNT, savedAppPlatMsgLog2.getMsgCount());
        assertNotNull(savedAppPlatMsgLog2.getId());
        assertTrue(savedAppPlatMsgLog2.getId() > 0);
        assertEquals(appPlat3.getId(), savedAppPlatMsgLog3.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog3.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog4.DATE, savedAppPlatMsgLog3.getDate());
        assertEquals(ApplicationPlatformMsgLog4.MSG_COUNT, savedAppPlatMsgLog3.getMsgCount());
        assertNotNull(savedAppPlatMsgLog3.getId());
        assertTrue(savedAppPlatMsgLog3.getId() > 0);
        assertEquals(appPlat3.getId(), savedAppPlatMsgLog4.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog4.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog3.DATE, savedAppPlatMsgLog4.getDate());
        assertEquals(ApplicationPlatformMsgLog3.MSG_COUNT, savedAppPlatMsgLog4.getMsgCount());
        assertNotNull(savedAppPlatMsgLog4.getId());
        assertTrue(savedAppPlatMsgLog4.getId() > 0);
        assertEquals(appPlat1.getId(), savedAppPlatMsgLog5.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog5.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog6.DATE, savedAppPlatMsgLog5.getDate());
        assertEquals(ApplicationPlatformMsgLog6.MSG_COUNT, savedAppPlatMsgLog5.getMsgCount());
        assertNotNull(savedAppPlatMsgLog5.getId());
        assertTrue(savedAppPlatMsgLog5.getId() > 0);
        assertEquals(appPlat1.getId(), savedAppPlatMsgLog6.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog6.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog5.DATE, savedAppPlatMsgLog6.getDate());
        assertEquals(ApplicationPlatformMsgLog5.MSG_COUNT, savedAppPlatMsgLog6.getMsgCount());
        assertNotNull(savedAppPlatMsgLog6.getId());
        assertTrue(savedAppPlatMsgLog6.getId() > 0);
    }

    /*
     * Insert 2 application platform message logs and then perform a save with 6
     * - 2 of which should be an update while the other 4 are inserts.
     */
    @Test
    public void testUpsertApp() throws Exception
    {
        appPlatMsgLog1 = ApplicationPlatformMsgLog1.getDomainObject();
        appPlatMsgLog2 = ApplicationPlatformMsgLog2.getDomainObject();
        appPlatMsgLog3 = ApplicationPlatformMsgLog3.getDomainObject();
        appPlatMsgLog4 = ApplicationPlatformMsgLog4.getDomainObject();
        appPlatMsgLog5 = ApplicationPlatformMsgLog5.getDomainObject();
        appPlatMsgLog6 = ApplicationPlatformMsgLog6.getDomainObject();
        appPlatMsgLog1.setApplicationPlatform(appPlat1);
        appPlatMsgLog2.setApplicationPlatform(appPlat1);
        appPlatMsgLog3.setApplicationPlatform(appPlat2);
        appPlatMsgLog4.setApplicationPlatform(appPlat2);
        appPlatMsgLog5.setApplicationPlatform(appPlat3);
        appPlatMsgLog6.setApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog1);
        TestDataHelper.createApplicationPlatformMsgLog(appPlatMsgLog2);

        appPlatMsgLog1.setApplicationPlatform(appPlat2);
        appPlatMsgLog1.setDate(ApplicationPlatformMsgLog2.DATE);
        appPlatMsgLog1.setMsgCount(ApplicationPlatformMsgLog2.MSG_COUNT);

        appPlatMsgLog2.setApplicationPlatform(appPlat2);
        appPlatMsgLog2.setDate(ApplicationPlatformMsgLog1.DATE);
        appPlatMsgLog2.setMsgCount(ApplicationPlatformMsgLog1.MSG_COUNT);

        ApplicationPlatformMsgLog[] msgLogs = { appPlatMsgLog1, appPlatMsgLog2, appPlatMsgLog3, appPlatMsgLog4,
                appPlatMsgLog5, appPlatMsgLog6 };
        List<ApplicationPlatformMsgLog> savedAppPlatMsgLogs = applicationPlatformMsgLogDAO
                .saveApplicationPlatformMsgLog(msgLogs);
        assertEquals(msgLogs.length, savedAppPlatMsgLogs.size());
        ApplicationPlatformMsgLog savedAppPlatMsgLog1 = null, savedAppPlatMsgLog2 = null, savedAppPlatMsgLog3 = null, savedAppPlatMsgLog4 = null, savedAppPlatMsgLog5 = null, savedAppPlatMsgLog6 = null;
        for (ApplicationPlatformMsgLog appPlatMsgLog : savedAppPlatMsgLogs) {
            if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog2.MSG_COUNT)) {
                savedAppPlatMsgLog1 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog1.MSG_COUNT)) {
                savedAppPlatMsgLog2 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog3.MSG_COUNT)) {
                savedAppPlatMsgLog3 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog4.MSG_COUNT)) {
                savedAppPlatMsgLog4 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog5.MSG_COUNT)) {
                savedAppPlatMsgLog5 = appPlatMsgLog;
            } else if (appPlatMsgLog.getMsgCount().equals(ApplicationPlatformMsgLog6.MSG_COUNT)) {
                savedAppPlatMsgLog6 = appPlatMsgLog;
            }
        }
        appPlatMsgLog3.setId(savedAppPlatMsgLog3.getId());
        appPlatMsgLog4.setId(savedAppPlatMsgLog4.getId());
        appPlatMsgLog5.setId(savedAppPlatMsgLog5.getId());
        appPlatMsgLog6.setId(savedAppPlatMsgLog6.getId());
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog1.getApplicationPlatform().getId());
        assertEquals(4, savedAppPlatMsgLog1.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog2.DATE, savedAppPlatMsgLog1.getDate());
        assertEquals(ApplicationPlatformMsgLog2.MSG_COUNT, savedAppPlatMsgLog1.getMsgCount());
        assertNotNull(savedAppPlatMsgLog1.getId());
        assertTrue(savedAppPlatMsgLog1.getId() > 0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog2.getApplicationPlatform().getId());
        assertEquals(4, savedAppPlatMsgLog2.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog1.DATE, savedAppPlatMsgLog2.getDate());
        assertEquals(ApplicationPlatformMsgLog1.MSG_COUNT, savedAppPlatMsgLog2.getMsgCount());
        assertNotNull(savedAppPlatMsgLog2.getId());
        assertTrue(savedAppPlatMsgLog2.getId() > 0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog3.getApplicationPlatform().getId());
        assertEquals(4, savedAppPlatMsgLog3.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog3.DATE, savedAppPlatMsgLog3.getDate());
        assertEquals(ApplicationPlatformMsgLog3.MSG_COUNT, savedAppPlatMsgLog3.getMsgCount());
        assertNotNull(savedAppPlatMsgLog3.getId());
        assertTrue(savedAppPlatMsgLog3.getId() > 0);
        assertEquals(appPlat2.getId(), savedAppPlatMsgLog4.getApplicationPlatform().getId());
        assertEquals(4, savedAppPlatMsgLog4.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog4.DATE, savedAppPlatMsgLog4.getDate());
        assertEquals(ApplicationPlatformMsgLog4.MSG_COUNT, savedAppPlatMsgLog4.getMsgCount());
        assertNotNull(savedAppPlatMsgLog4.getId());
        assertTrue(savedAppPlatMsgLog4.getId() > 0);
        assertEquals(appPlat3.getId(), savedAppPlatMsgLog5.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog5.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog5.DATE, savedAppPlatMsgLog5.getDate());
        assertEquals(ApplicationPlatformMsgLog5.MSG_COUNT, savedAppPlatMsgLog5.getMsgCount());
        assertNotNull(savedAppPlatMsgLog5.getId());
        assertTrue(savedAppPlatMsgLog5.getId() > 0);
        assertEquals(appPlat3.getId(), savedAppPlatMsgLog6.getApplicationPlatform().getId());
        assertEquals(2, savedAppPlatMsgLog6.getApplicationPlatform().getApplicationPlatformMsgLogs().size());
        assertEquals(ApplicationPlatformMsgLog6.DATE, savedAppPlatMsgLog6.getDate());
        assertEquals(ApplicationPlatformMsgLog6.MSG_COUNT, savedAppPlatMsgLog6.getMsgCount());
        assertNotNull(savedAppPlatMsgLog6.getId());
        assertTrue(savedAppPlatMsgLog6.getId() > 0);
    }
}

