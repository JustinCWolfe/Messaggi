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
import com.messaggi.TestDataHelper.ApplicationPlatform4;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;

public class TestDeviceDAO extends MessaggiTestCase
{
    private Application app1;

    private ApplicationPlatform appPlat1;

    private ApplicationPlatform appPlat2;

    private ApplicationPlatform appPlat3;

    private Device d1;

    private Device d2;

    private Device d3;

    private Device d4;

    private Device d5;

    private Device d6;

    private DeviceDAO deviceDAO;

    private User user1;

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
        deviceDAO = new DeviceDAO();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteDevice(d1);
        TestDataHelper.deleteDevice(d2);
        TestDataHelper.deleteDevice(d3);
        TestDataHelper.deleteDevice(d4);
        TestDataHelper.deleteDevice(d5);
        TestDataHelper.deleteDevice(d6);
        TestDataHelper.deleteApplicationPlatform(appPlat1);
        TestDataHelper.deleteApplicationPlatform(appPlat2);
        TestDataHelper.deleteApplicationPlatform(appPlat3);
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteUser(user1);
    }

    @Test
    public void testGetDeviceByCode() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);
        TestDataHelper.createApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatform(appPlat4);
        appPlat1.setApplication(null);
        appPlat2.setApplication(null);
        appPlat3.setApplication(null);
        appPlat4.setApplication(null);
        Device[] appPlats = { appPlat1, appPlat1, appPlat2, appPlat2, appPlat3, appPlat3, appPlat4,
                appPlat4 };
        List<Device> retrievedAppPlats = deviceDAO.getDevice(appPlats);
        Device retrievedAppPlat1 = null, retrievedAppPlat2 = null, retrievedAppPlat3 = null, retrievedAppPlat4 = null;
        for (Device appPlat : retrievedAppPlats) {
            if (appPlat.getToken().equals(ApplicationPlatform1.TOKEN)) {
                retrievedAppPlat1 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform2.TOKEN)) {
                retrievedAppPlat2 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform3.TOKEN)) {
                retrievedAppPlat3 = appPlat;
            } else {
                retrievedAppPlat4 = appPlat;
            }
        }
        assertEquals(4, retrievedAppPlats.size());
        assertEquals(appPlat1.getId(), retrievedAppPlat1.getId());
        assertEquals(app1.getId(), retrievedAppPlat1.getApplication().getId());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat1.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat1.getToken());
        assertEquals(appPlat2.getId(), retrievedAppPlat2.getId());
        assertEquals(app1.getId(), retrievedAppPlat2.getApplication().getId());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat2.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat2.getToken());
        assertEquals(appPlat3.getId(), retrievedAppPlat3.getId());
        assertEquals(app1.getId(), retrievedAppPlat3.getApplication().getId());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat3.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat3.getToken());
        assertEquals(appPlat4.getId(), retrievedAppPlat4.getId());
        assertEquals(app2.getId(), retrievedAppPlat4.getApplication().getId());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat4.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat4.getToken());
    }

    @Test
    public void testGetDeviceByApplicationPlatformId() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);
        TestDataHelper.createApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatform(appPlat4);
        ApplicationPlatform noIDAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform noIDAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform noIDAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform noIDAppPlat4 = ApplicationPlatform4.getDomainObject();
        noIDAppPlat1.setApplication(app1);
        noIDAppPlat2.setApplication(app1);
        noIDAppPlat3.setApplication(app1);
        noIDAppPlat4.setApplication(app2);
        Device[] appPlats = { noIDAppPlat1, noIDAppPlat1, noIDAppPlat2, noIDAppPlat2, noIDAppPlat3,
                noIDAppPlat3, noIDAppPlat4, noIDAppPlat4 };
        List<Device> retrievedAppPlats = deviceDAO.getDevice(appPlats);
        Device retrievedAppPlat1 = null, retrievedAppPlat2 = null, retrievedAppPlat3 = null, retrievedAppPlat4 = null;
        for (Device appPlat : retrievedAppPlats) {
            if (appPlat.getToken().equals(ApplicationPlatform1.TOKEN)) {
                retrievedAppPlat1 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform2.TOKEN)) {
                retrievedAppPlat2 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform3.TOKEN)) {
                retrievedAppPlat3 = appPlat;
            } else {
                retrievedAppPlat4 = appPlat;
            }
        }
        assertEquals(4, retrievedAppPlats.size());
        assertEquals(appPlat1.getId(), retrievedAppPlat1.getId());
        assertEquals(appPlat1.getApplication().getId(), retrievedAppPlat1.getApplication().getId());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat1.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat1.getToken());
        assertEquals(appPlat2.getId(), retrievedAppPlat2.getId());
        assertEquals(appPlat2.getApplication().getId(), retrievedAppPlat2.getApplication().getId());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat2.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat2.getToken());
        assertEquals(appPlat3.getId(), retrievedAppPlat3.getId());
        assertEquals(appPlat2.getApplication().getId(), retrievedAppPlat3.getApplication().getId());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat3.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat3.getToken());
        assertEquals(appPlat4.getId(), retrievedAppPlat4.getId());
        assertEquals(appPlat4.getApplication().getId(), retrievedAppPlat4.getApplication().getId());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat4.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat4.getToken());
    }

    @Test
    public void testGetDeviceByIdAndApplicationPlatformId() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat1.setApplication(app1);
        Device[] appPlats1 = { appPlat1 };
        List<Device> retrievedAppPlats1 = deviceDAO.getDevice(appPlats1);
        assertEquals(0, retrievedAppPlats1.size());

        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);
        Device[] appPlats2 = { appPlat1, appPlat1, appPlat2, appPlat2, appPlat3, appPlat3, appPlat4,
                appPlat4 };
        List<Device> retrievedAppPlats2 = deviceDAO.getDevice(appPlats2);
        assertEquals(0, retrievedAppPlats2.size());

        TestDataHelper.createApplicationPlatform(appPlat1);
        List<Device> retrievedAppPlats3 = deviceDAO.getDevice(appPlats2);
        Device retrievedAppPlat31 = retrievedAppPlats3.get(0);
        assertEquals(1, retrievedAppPlats3.size());
        assertEquals(appPlat1.getId(), retrievedAppPlat31.getId());
        assertEquals(appPlat1.getApplication().getId(), retrievedAppPlat31.getApplication().getId());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat31.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat31.getToken());

        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatform(appPlat4);
        List<Device> retrievedAppPlats4 = deviceDAO.getDevice(appPlats2);
        Device retrievedAppPlat41 = null, retrievedAppPlat42 = null, retrievedAppPlat43 = null, retrievedAppPlat44 = null;
        for (Device appPlat : retrievedAppPlats4) {
            if (appPlat.getToken().equals(ApplicationPlatform1.TOKEN)) {
                retrievedAppPlat41 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform2.TOKEN)) {
                retrievedAppPlat42 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform3.TOKEN)) {
                retrievedAppPlat43 = appPlat;
            } else {
                retrievedAppPlat44 = appPlat;
            }
        }
        assertEquals(4, retrievedAppPlats4.size());
        assertEquals(appPlat1.getId(), retrievedAppPlat41.getId());
        assertEquals(appPlat1.getApplication().getId(), retrievedAppPlat41.getApplication().getId());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat41.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat41.getToken());
        assertEquals(appPlat2.getId(), retrievedAppPlat42.getId());
        assertEquals(appPlat2.getApplication().getId(), retrievedAppPlat42.getApplication().getId());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat42.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat42.getToken());
        assertEquals(appPlat3.getId(), retrievedAppPlat43.getId());
        assertEquals(appPlat3.getApplication().getId(), retrievedAppPlat43.getApplication().getId());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat43.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat43.getToken());
        assertEquals(appPlat4.getId(), retrievedAppPlat44.getId());
        assertEquals(appPlat4.getApplication().getId(), retrievedAppPlat44.getApplication().getId());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat44.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat44.getToken());
    }

    @Test
    public void testSaveNonExistentDeviceSingle() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat1.setApplication(app1);

        Device[] appPlats = { appPlat1 };
        List<Device> savedAppPlats = deviceDAO.saveDevice(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        Device savedAppPlat1 = savedAppPlats.get(0);
        appPlat1.setId(savedAppPlat1.getId());
        assertEquals(ApplicationPlatform1.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform1.TOKEN, savedAppPlat1.getToken());
        assertEquals(app1.getId(), savedAppPlat1.getApplication().getId());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
    }

    @Test
    public void testSaveNonExistentDeviceMultiple() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);

        Device[] appPlats = { appPlat1, appPlat2, appPlat3, appPlat4 };
        List<Device> savedAppPlats = deviceDAO.saveDevice(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        Device savedAppPlat1 = null, savedAppPlat2 = null, savedAppPlat3 = null, savedAppPlat4 = null;
        for (Device appPlat : savedAppPlats) {
            if (appPlat.getToken().equals(ApplicationPlatform1.TOKEN)) {
                savedAppPlat1 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform2.TOKEN)) {
                savedAppPlat2 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform3.TOKEN)) {
                savedAppPlat3 = appPlat;
            } else {
                savedAppPlat4 = appPlat;
            }
        }
        appPlat1.setId(savedAppPlat1.getId());
        appPlat2.setId(savedAppPlat2.getId());
        appPlat3.setId(savedAppPlat3.getId());
        appPlat4.setId(savedAppPlat4.getId());
        assertEquals(ApplicationPlatform1.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform1.TOKEN, savedAppPlat1.getToken());
        assertEquals(app1.getId(), savedAppPlat1.getApplication().getId());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat2.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat2.getToken());
        assertEquals(app1.getId(), savedAppPlat2.getApplication().getId());
        assertNotNull(savedAppPlat2.getId());
        assertTrue(savedAppPlat2.getId() > 0);
        assertEquals(ApplicationPlatform3.PLATFORM, savedAppPlat3.getPlatform());
        assertEquals(ApplicationPlatform3.TOKEN, savedAppPlat3.getToken());
        assertEquals(app1.getId(), savedAppPlat3.getApplication().getId());
        assertNotNull(savedAppPlat3.getId());
        assertTrue(savedAppPlat3.getId() > 0);
        assertEquals(ApplicationPlatform4.PLATFORM, savedAppPlat4.getPlatform());
        assertEquals(ApplicationPlatform4.TOKEN, savedAppPlat4.getToken());
        assertEquals(app2.getId(), savedAppPlat4.getApplication().getId());
        assertNotNull(savedAppPlat4.getId());
        assertTrue(savedAppPlat4.getId() > 0);
    }

    @Test
    public void testSaveExistingDeviceSingle() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat1.setApplication(app1);
        TestDataHelper.createApplicationPlatform(appPlat1);

        appPlat1.setToken(ApplicationPlatform2.TOKEN);
        appPlat1.setPlatform(ApplicationPlatform2.PLATFORM);
        appPlat1.setApplication(app2);

        Device[] appPlats = { appPlat1 };
        List<Device> savedAppPlats = deviceDAO.saveDevice(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        Device savedAppPlat1 = savedAppPlats.get(0);
        appPlat1.setId(savedAppPlat1.getId());
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat1.getToken());
        assertEquals(app2.getId(), savedAppPlat1.getApplication().getId());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
    }

    @Test
    public void testSaveExistingDeviceMultiple() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app2);
        TestDataHelper.createApplicationPlatform(appPlat1);
        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);

        appPlat1.setToken(ApplicationPlatform3.TOKEN);
        appPlat1.setPlatform(ApplicationPlatform3.PLATFORM);
        appPlat1.setApplication(app2);

        appPlat2.setToken(ApplicationPlatform1.TOKEN);
        appPlat2.setPlatform(ApplicationPlatform1.PLATFORM);
        appPlat2.setApplication(app2);

        appPlat3.setToken(ApplicationPlatform2.TOKEN);
        appPlat3.setPlatform(ApplicationPlatform2.PLATFORM);
        appPlat3.setApplication(app1);

        Device[] appPlats = { appPlat1, appPlat2, appPlat3 };
        List<Device> savedAppPlats = deviceDAO.saveDevice(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        Device savedAppPlat1 = null, savedAppPlat2 = null, savedAppPlat3 = null;
        for (Device appPlat : savedAppPlats) {
            if (appPlat.getToken().equals(ApplicationPlatform3.TOKEN)) {
                savedAppPlat1 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform1.TOKEN)) {
                savedAppPlat2 = appPlat;
            } else {
                savedAppPlat3 = appPlat;
            }
        }
        appPlat1.setId(savedAppPlat1.getId());
        appPlat2.setId(savedAppPlat2.getId());
        appPlat3.setId(savedAppPlat3.getId());
        assertEquals(ApplicationPlatform3.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform3.TOKEN, savedAppPlat1.getToken());
        assertEquals(app2.getId(), savedAppPlat1.getApplication().getId());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
        assertEquals(ApplicationPlatform1.PLATFORM, savedAppPlat2.getPlatform());
        assertEquals(ApplicationPlatform1.TOKEN, savedAppPlat2.getToken());
        assertEquals(app2.getId(), savedAppPlat2.getApplication().getId());
        assertNotNull(savedAppPlat2.getId());
        assertTrue(savedAppPlat2.getId() > 0);
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat3.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat3.getToken());
        assertEquals(app1.getId(), savedAppPlat3.getApplication().getId());
        assertNotNull(savedAppPlat3.getId());
        assertTrue(savedAppPlat3.getId() > 0);
    }

    /*
     * Insert 2 devices and then perform a save with 6 - 2 of which should be an
     * update while the others are inserts.
     */
    @Test
    public void testUpsertDevice() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app2);
        TestDataHelper.createApplicationPlatform(appPlat1);

        appPlat1.setToken(ApplicationPlatform4.TOKEN);
        appPlat1.setPlatform(ApplicationPlatform4.PLATFORM);
        appPlat1.setApplication(app2);

        Device[] appPlats = { appPlat1, appPlat2, appPlat3 };
        List<Device> savedAppPlats = deviceDAO.saveDevice(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        Device savedAppPlat1 = null, savedAppPlat2 = null, savedAppPlat3 = null;
        for (Device appPlat : savedAppPlats) {
            if (appPlat.getToken().equals(ApplicationPlatform4.TOKEN)) {
                savedAppPlat1 = appPlat;
            } else if (appPlat.getToken().equals(ApplicationPlatform2.TOKEN)) {
                savedAppPlat2 = appPlat;
            } else {
                savedAppPlat3 = appPlat;
            }
        }
        appPlat1.setId(savedAppPlat1.getId());
        appPlat2.setId(savedAppPlat2.getId());
        appPlat3.setId(savedAppPlat3.getId());
        assertEquals(ApplicationPlatform4.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform4.TOKEN, savedAppPlat1.getToken());
        assertEquals(app2.getId(), savedAppPlat1.getApplication().getId());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat2.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat2.getToken());
        assertEquals(app1.getId(), savedAppPlat2.getApplication().getId());
        assertNotNull(savedAppPlat2.getId());
        assertTrue(savedAppPlat2.getId() > 0);
        assertEquals(ApplicationPlatform3.PLATFORM, savedAppPlat3.getPlatform());
        assertEquals(ApplicationPlatform3.TOKEN, savedAppPlat3.getToken());
        assertEquals(app2.getId(), savedAppPlat3.getApplication().getId());
        assertNotNull(savedAppPlat3.getId());
        assertTrue(savedAppPlat3.getId() > 0);
    }
}

