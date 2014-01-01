package com.messaggi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Application1;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.Device1;
import com.messaggi.TestDataHelper.Device2;
import com.messaggi.TestDataHelper.Device3;
import com.messaggi.TestDataHelper.Device4;
import com.messaggi.TestDataHelper.Device5;
import com.messaggi.TestDataHelper.Device6;
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
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        ApplicationPlatform[] appPlats2 = { appPlat1, appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat1, appPlat2, appPlat3 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        TestDataHelper.createDevice(d1);
        TestDataHelper.createDevice(d2);
        TestDataHelper.createDevice(d3);
        TestDataHelper.createDevice(d4);
        TestDataHelper.createDevice(d5);
        TestDataHelper.createDevice(d6);
        d1.setApplicationPlatforms(null);
        d2.setApplicationPlatforms(null);
        d3.setApplicationPlatforms(null);
        d4.setApplicationPlatforms(null);
        d5.setApplicationPlatforms(null);
        d6.setApplicationPlatforms(null);

        Device[] devices = { d1, d1, d2, d2, d3, d3, d4, d4, d5, d5, d6, d6 };
        List<Device> retrievedDevices = deviceDAO.getDevice(devices);
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        Device retrievedDevice1 = null, retrievedDevice2 = null, retrievedDevice3 = null, retrievedDevice4 = null, retrievedDevice5 = null, retrievedDevice6 = null;
        for (Device d : retrievedDevices) {
            if (d.getCode().equals(Device1.CODE)) {
                retrievedDevice1 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                retrievedDevice2 = d;
            } else if (d.getCode().equals(Device3.CODE)) {
                retrievedDevice3 = d;
            } else if (d.getCode().equals(Device4.CODE)) {
                retrievedDevice4 = d;
            } else if (d.getCode().equals(Device5.CODE)) {
                retrievedDevice5 = d;
            } else if (d.getCode().equals(Device6.CODE)) {
                retrievedDevice6 = d;
            }
        }
        assertEquals(true, retrievedDevice1.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice1.getApplicationPlatformId());
        ApplicationPlatform retrievedAppPlat11 = null;
        for (ApplicationPlatform ap : retrievedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat11 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat11.getId());
        assertEquals(6, retrievedAppPlat11.getDevices().size());

        assertEquals(true, retrievedDevice2.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice2.getApplicationPlatformId());
        ApplicationPlatform retrievedAppPlat21 = null;
        for (ApplicationPlatform ap : retrievedDevice2.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat21 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat21.getId());
        assertEquals(6, retrievedAppPlat21.getDevices().size());

        assertEquals(true, retrievedDevice3.getActive());
        assertEquals(2, retrievedDevice3.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat31 = null, retrievedAppPlat32 = null;
        for (ApplicationPlatform ap : retrievedDevice3.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat31 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat32 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat31.getId());
        assertEquals(6, retrievedAppPlat31.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat32.getId());
        assertEquals(4, retrievedAppPlat32.getDevices().size());

        assertEquals(true, retrievedDevice4.getActive());
        assertEquals(2, retrievedDevice4.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat41 = null, retrievedAppPlat42 = null;
        for (ApplicationPlatform ap : retrievedDevice4.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat41 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat42 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat41.getId());
        assertEquals(6, retrievedAppPlat41.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat42.getId());
        assertEquals(4, retrievedAppPlat42.getDevices().size());

        assertEquals(true, retrievedDevice5.getActive());
        assertEquals(3, retrievedDevice5.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat51 = null, retrievedAppPlat52 = null, retrievedAppPlat53 = null;
        for (ApplicationPlatform ap : retrievedDevice5.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat51 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat52 = ap;
            } else if (ap.getId().equals(appPlat3.getId())) {
                retrievedAppPlat53 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat51.getId());
        assertEquals(6, retrievedAppPlat51.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat52.getId());
        assertEquals(4, retrievedAppPlat52.getDevices().size());
        assertEquals(appPlat3.getId(), retrievedAppPlat53.getId());
        assertEquals(2, retrievedAppPlat53.getDevices().size());

        assertEquals(true, retrievedDevice6.getActive());
        assertEquals(3, retrievedDevice6.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat61 = null, retrievedAppPlat62 = null, retrievedAppPlat63 = null;
        for (ApplicationPlatform ap : retrievedDevice6.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat61 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat62 = ap;
            } else if (ap.getId().equals(appPlat3.getId())) {
                retrievedAppPlat63 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat61.getId());
        assertEquals(6, retrievedAppPlat61.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat62.getId());
        assertEquals(4, retrievedAppPlat62.getDevices().size());
        assertEquals(appPlat3.getId(), retrievedAppPlat63.getId());
        assertEquals(2, retrievedAppPlat63.getDevices().size());
    }

    @Test
    public void testGetDeviceByApplicationPlatformId() throws Exception
    {
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        ApplicationPlatform[] appPlats2 = { appPlat1, appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat1, appPlat2, appPlat3 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        TestDataHelper.createDevice(d1);
        TestDataHelper.createDevice(d2);
        TestDataHelper.createDevice(d3);
        TestDataHelper.createDevice(d4);
        TestDataHelper.createDevice(d5);
        TestDataHelper.createDevice(d6);

        Device deviceWithoutCode1 = Device1.getDomainObject();
        deviceWithoutCode1.setCode(null);
        deviceWithoutCode1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));

        Device[] devices = { deviceWithoutCode1, deviceWithoutCode1 };
        List<Device> retrievedDevices1 = deviceDAO.getDevice(devices);
        Device retrievedDevice11 = null, retrievedDevice12 = null, retrievedDevice13 = null, retrievedDevice14 = null, retrievedDevice15 = null, retrievedDevice16 = null;
        for (Device d : retrievedDevices1) {
            if (d.getCode().equals(Device1.CODE)) {
                retrievedDevice11 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                retrievedDevice12 = d;
            } else if (d.getCode().equals(Device3.CODE)) {
                retrievedDevice13 = d;
            } else if (d.getCode().equals(Device4.CODE)) {
                retrievedDevice14 = d;
            } else if (d.getCode().equals(Device5.CODE)) {
                retrievedDevice15 = d;
            } else if (d.getCode().equals(Device6.CODE)) {
                retrievedDevice16 = d;
            }
        }
        assertEquals(true, retrievedDevice11.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice11.getApplicationPlatformId());
        ApplicationPlatform retrievedAppPlat111 = null;
        for (ApplicationPlatform ap : retrievedDevice11.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat111 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat111.getId());
        assertEquals(6, retrievedAppPlat111.getDevices().size());

        assertEquals(true, retrievedDevice12.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice12.getApplicationPlatformId());
        ApplicationPlatform retrievedAppPlat121 = null;
        for (ApplicationPlatform ap : retrievedDevice12.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat121 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat121.getId());
        assertEquals(6, retrievedAppPlat121.getDevices().size());

        assertEquals(true, retrievedDevice13.getActive());
        assertEquals(2, retrievedDevice13.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat131 = null, retrievedAppPlat132 = null;
        for (ApplicationPlatform ap : retrievedDevice13.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat131 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat132 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat131.getId());
        assertEquals(6, retrievedAppPlat131.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat132.getId());
        assertEquals(4, retrievedAppPlat132.getDevices().size());

        assertEquals(true, retrievedDevice14.getActive());
        assertEquals(2, retrievedDevice14.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat141 = null, retrievedAppPlat142 = null;
        for (ApplicationPlatform ap : retrievedDevice14.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat141 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat142 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat141.getId());
        assertEquals(6, retrievedAppPlat141.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat142.getId());
        assertEquals(4, retrievedAppPlat142.getDevices().size());

        assertEquals(true, retrievedDevice15.getActive());
        assertEquals(3, retrievedDevice15.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat151 = null, retrievedAppPlat152 = null, retrievedAppPlat153 = null;
        for (ApplicationPlatform ap : retrievedDevice15.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat151 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat152 = ap;
            } else if (ap.getId().equals(appPlat3.getId())) {
                retrievedAppPlat153 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat151.getId());
        assertEquals(6, retrievedAppPlat151.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat152.getId());
        assertEquals(4, retrievedAppPlat152.getDevices().size());
        assertEquals(appPlat3.getId(), retrievedAppPlat153.getId());
        assertEquals(2, retrievedAppPlat153.getDevices().size());

        assertEquals(true, retrievedDevice16.getActive());
        assertEquals(3, retrievedDevice16.getApplicationPlatforms().size());
        ApplicationPlatform retrievedAppPlat161 = null, retrievedAppPlat162 = null, retrievedAppPlat163 = null;
        for (ApplicationPlatform ap : retrievedDevice16.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                retrievedAppPlat161 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                retrievedAppPlat162 = ap;
            } else if (ap.getId().equals(appPlat3.getId())) {
                retrievedAppPlat163 = ap;
            }
        }
        assertEquals(appPlat1.getId(), retrievedAppPlat161.getId());
        assertEquals(6, retrievedAppPlat161.getDevices().size());
        assertEquals(appPlat2.getId(), retrievedAppPlat162.getId());
        assertEquals(4, retrievedAppPlat162.getDevices().size());
        assertEquals(appPlat3.getId(), retrievedAppPlat163.getId());
        assertEquals(2, retrievedAppPlat163.getDevices().size());
    }

    @Test
    public void testGetDeviceByIdAndApplicationPlatformId() throws Exception
    {
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));

        Device[] devices1 = { d1, d1 };
        List<Device> retrievedDevices1 = deviceDAO.getDevice(devices1);
        assertEquals(0, retrievedDevices1.size());

        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats2 = { appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat3 };
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));

        Device[] devices2 = { d1, d1, d2, d2, d3, d3, d4, d4, d5, d5, d6, d6 };
        List<Device> retrievedDevices2 = deviceDAO.getDevice(devices2);
        assertEquals(0, retrievedDevices2.size());

        TestDataHelper.createDevice(d1);

        List<Device> retrievedDevices3 = deviceDAO.getDevice(devices2);
        assertEquals(1, retrievedDevices3.size());
        Device retrievedDevice31 = retrievedDevices3.get(0);
        assertEquals(true, retrievedDevice31.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice31.getApplicationPlatformId());

        Device deviceWithoutCode1 = Device1.getDomainObject();
        deviceWithoutCode1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        Device[] devices3 = { deviceWithoutCode1, deviceWithoutCode1 };
        List<Device> retrievedDevices4 = deviceDAO.getDevice(devices3);
        assertEquals(1, retrievedDevices4.size());
        Device retrievedDevice41 = retrievedDevices3.get(0);
        assertEquals(true, retrievedDevice41.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice41.getApplicationPlatformId());

        TestDataHelper.createDevice(d2);
        List<Device> retrievedDevices5 = deviceDAO.getDevice(devices2);
        Device retrievedDevice51 = null, retrievedDevice52 = null;
        for (Device d : retrievedDevices5) {
            if (d.getCode().equals(Device1.CODE)) {
                retrievedDevice51 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                retrievedDevice52 = d;
            }
        }
        assertEquals(true, retrievedDevice51.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice51.getApplicationPlatformId());

        assertEquals(true, retrievedDevice52.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice52.getApplicationPlatformId());

        TestDataHelper.createDevice(d3);
        TestDataHelper.createDevice(d4);
        TestDataHelper.createDevice(d5);
        TestDataHelper.createDevice(d6);
        List<Device> retrievedDevices6 = deviceDAO.getDevice(devices2);
        Device retrievedDevice61 = null, retrievedDevice62 = null, retrievedDevice63 = null, retrievedDevice64 = null, retrievedDevice65 = null, retrievedDevice66 = null;
        for (Device d : retrievedDevices6) {
            if (d.getCode().equals(Device1.CODE)) {
                retrievedDevice61 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                retrievedDevice62 = d;
            } else if (d.getCode().equals(Device3.CODE)) {
                retrievedDevice63 = d;
            } else if (d.getCode().equals(Device4.CODE)) {
                retrievedDevice64 = d;
            } else if (d.getCode().equals(Device5.CODE)) {
                retrievedDevice65 = d;
            } else if (d.getCode().equals(Device6.CODE)) {
                retrievedDevice66 = d;
            }
        }
        assertEquals(true, retrievedDevice61.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice61.getApplicationPlatformId());

        assertEquals(true, retrievedDevice62.getActive());
        assertEquals(appPlat1.getId(), retrievedDevice62.getApplicationPlatformId());

        assertEquals(true, retrievedDevice63.getActive());
        assertEquals(1, retrievedDevice63.getApplicationPlatforms().size());
        assertEquals(appPlat2.getId(), retrievedDevice63.getApplicationPlatformId());

        assertEquals(true, retrievedDevice64.getActive());
        assertEquals(1, retrievedDevice64.getApplicationPlatforms().size());
        assertEquals(appPlat2.getId(), retrievedDevice64.getApplicationPlatformId());

        assertEquals(true, retrievedDevice65.getActive());
        assertEquals(1, retrievedDevice65.getApplicationPlatforms().size());
        assertEquals(appPlat3.getId(), retrievedDevice65.getApplicationPlatformId());

        assertEquals(true, retrievedDevice66.getActive());
        assertEquals(1, retrievedDevice66.getApplicationPlatforms().size());
        assertEquals(appPlat3.getId(), retrievedDevice66.getApplicationPlatformId());
    }

    @Test
    public void testSaveNonExistentDeviceSingle() throws Exception
    {
        d1 = Device1.getDomainObject();
        ApplicationPlatform[] appPlats = { appPlat1 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats)));

        Device[] devices = { d1 };
        List<Device> savedDevices = deviceDAO.saveDevice(devices);
        assertEquals(devices.length, savedDevices.size());
        Device savedDevice1 = savedDevices.get(0);
        assertEquals(Device1.CODE, savedDevice1.getCode());
        assertEquals(true, savedDevice1.getActive());
        assertEquals(appPlat1.getId(), savedDevice1.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat1 = null;
        for (ApplicationPlatform ap : savedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat1 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat1.getId());
        assertEquals(1, savedAppPlat1.getDevices().size());
    }

    @Test
    public void testSaveNonExistentDeviceSingleException() throws Exception
    {
        d1 = Device1.getDomainObject();
        ApplicationPlatform[] appPlats = { appPlat1, appPlat2 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats)));

        Device[] devices = { d1 };
        try {
            // Should throw because save of a device with multiple application platforms is not valid.
            deviceDAO.saveDevice(devices);
            fail();
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void testSaveNonExistentDeviceMultiple() throws Exception
    {
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        ApplicationPlatform[] appPlats2 = { appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat3 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));

        Device[] devices = { d1, d2, d3, d4, d5, d6 };
        List<Device> savedDevices = deviceDAO.saveDevice(devices);
        assertEquals(devices.length, savedDevices.size());
        Device savedDevice1 = null, savedDevice2 = null, savedDevice3 = null, savedDevice4 = null, savedDevice5 = null, savedDevice6 = null;
        for (Device d : savedDevices) {
            if (d.getCode().equals(Device1.CODE)) {
                savedDevice1 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                savedDevice2 = d;
            } else if (d.getCode().equals(Device3.CODE)) {
                savedDevice3 = d;
            } else if (d.getCode().equals(Device4.CODE)) {
                savedDevice4 = d;
            } else if (d.getCode().equals(Device5.CODE)) {
                savedDevice5 = d;
            } else if (d.getCode().equals(Device6.CODE)) {
                savedDevice6 = d;
            }
        }
        assertEquals(true, savedDevice1.getActive());
        assertEquals(appPlat1.getId(), savedDevice1.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat11 = null;
        for (ApplicationPlatform ap : savedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat11 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat11.getId());
        assertEquals(2, savedAppPlat11.getDevices().size());

        assertEquals(true, savedDevice2.getActive());
        assertEquals(appPlat1.getId(), savedDevice2.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat21 = null;
        for (ApplicationPlatform ap : savedDevice2.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat21 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat21.getId());
        assertEquals(2, savedAppPlat21.getDevices().size());

        assertEquals(true, savedDevice3.getActive());
        assertEquals(appPlat2.getId(), savedDevice3.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat31 = null;
        for (ApplicationPlatform ap : savedDevice3.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat31 = ap;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlat31.getId());
        assertEquals(2, savedAppPlat31.getDevices().size());

        assertEquals(true, savedDevice4.getActive());
        assertEquals(appPlat2.getId(), savedDevice4.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat41 = null;
        for (ApplicationPlatform ap : savedDevice4.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat41 = ap;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlat41.getId());
        assertEquals(2, savedAppPlat41.getDevices().size());

        assertEquals(true, savedDevice5.getActive());
        assertEquals(appPlat3.getId(), savedDevice5.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat51 = null;
        for (ApplicationPlatform ap : savedDevice5.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat51 = ap;
            }
        }
        assertEquals(appPlat3.getId(), savedAppPlat51.getId());
        assertEquals(2, savedAppPlat51.getDevices().size());

        assertEquals(true, savedDevice6.getActive());
        assertEquals(appPlat3.getId(), savedDevice6.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat61 = null;
        for (ApplicationPlatform ap : savedDevice6.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat61 = ap;
            }
        }
        assertEquals(appPlat3.getId(), savedAppPlat61.getId());
        assertEquals(2, savedAppPlat61.getDevices().size());
    }

    @Test
    public void testSaveExistingDeviceSingle() throws Exception
    {
        d1 = Device1.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        TestDataHelper.createDevice(d1);

        d1.setActive(false);
        ApplicationPlatform[] appPlats2 = { appPlat2 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));

        Device[] devices = { d1 };
        List<Device> savedDevices = deviceDAO.saveDevice(devices);
        assertEquals(devices.length, savedDevices.size());
        Device savedDevice1 = savedDevices.get(0);
        assertEquals(Device1.CODE, savedDevice1.getCode());
        assertEquals(false, savedDevice1.getActive());
        assertEquals(2, savedDevice1.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat1 = null, savedAppPlat2 = null;
        for (ApplicationPlatform ap : savedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat1 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat2 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat1.getId());
        assertEquals(1, savedAppPlat1.getDevices().size());
        assertEquals(appPlat2.getId(), savedAppPlat2.getId());
        assertEquals(1, savedAppPlat2.getDevices().size());
    }

    @Test
    public void testSaveExistingDeviceMultiple() throws Exception
    {
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        ApplicationPlatform[] appPlats2 = { appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat3 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        TestDataHelper.createDevice(d1);
        TestDataHelper.createDevice(d2);
        TestDataHelper.createDevice(d3);
        TestDataHelper.createDevice(d4);
        TestDataHelper.createDevice(d5);
        TestDataHelper.createDevice(d6);

        d1.setActive(false);
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));

        d2.setActive(false);
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));

        d3.setActive(false);
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));

        d4.setActive(false);
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));

        d5.setActive(false);
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));

        d6.setActive(false);
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));

        Device[] devices = { d1, d2, d3, d4, d5, d6 };
        List<Device> savedDevices = deviceDAO.saveDevice(devices);
        assertEquals(devices.length, savedDevices.size());
        Device savedDevice1 = null, savedDevice2 = null, savedDevice3 = null, savedDevice4 = null, savedDevice5 = null, savedDevice6 = null;
        for (Device d : savedDevices) {
            if (d.getCode().equals(Device1.CODE)) {
                savedDevice1 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                savedDevice2 = d;
            } else if (d.getCode().equals(Device3.CODE)) {
                savedDevice3 = d;
            } else if (d.getCode().equals(Device4.CODE)) {
                savedDevice4 = d;
            } else if (d.getCode().equals(Device5.CODE)) {
                savedDevice5 = d;
            } else if (d.getCode().equals(Device6.CODE)) {
                savedDevice6 = d;
            }
        }
        assertEquals(false, savedDevice1.getActive());
        assertEquals(2, savedDevice1.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat11 = null, savedAppPlat12 = null;
        for (ApplicationPlatform ap : savedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat11 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat12 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat11.getId());
        assertEquals(4, savedAppPlat11.getDevices().size());
        assertEquals(appPlat2.getId(), savedAppPlat12.getId());
        assertEquals(4, savedAppPlat12.getDevices().size());

        assertEquals(false, savedDevice2.getActive());
        assertEquals(2, savedDevice2.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat21 = null, savedAppPlat22 = null;
        for (ApplicationPlatform ap : savedDevice2.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat21 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat22 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat21.getId());
        assertEquals(4, savedAppPlat21.getDevices().size());
        assertEquals(appPlat2.getId(), savedAppPlat22.getId());
        assertEquals(4, savedAppPlat22.getDevices().size());

        assertEquals(false, savedDevice3.getActive());
        assertEquals(2, savedDevice3.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat31 = null, savedAppPlat32 = null;
        for (ApplicationPlatform ap : savedDevice3.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat31 = ap;
            } else if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat32 = ap;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlat31.getId());
        assertEquals(4, savedAppPlat31.getDevices().size());
        assertEquals(appPlat3.getId(), savedAppPlat32.getId());
        assertEquals(4, savedAppPlat32.getDevices().size());

        assertEquals(false, savedDevice4.getActive());
        assertEquals(2, savedDevice4.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat41 = null, savedAppPlat42 = null;
        for (ApplicationPlatform ap : savedDevice4.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat41 = ap;
            } else if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat42 = ap;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlat41.getId());
        assertEquals(4, savedAppPlat41.getDevices().size());
        assertEquals(appPlat3.getId(), savedAppPlat42.getId());
        assertEquals(4, savedAppPlat42.getDevices().size());

        assertEquals(false, savedDevice5.getActive());
        assertEquals(2, savedDevice5.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat51 = null, savedAppPlat52 = null;
        for (ApplicationPlatform ap : savedDevice5.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat51 = ap;
            } else if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat52 = ap;
            }
        }
        assertEquals(appPlat3.getId(), savedAppPlat51.getId());
        assertEquals(4, savedAppPlat51.getDevices().size());
        assertEquals(appPlat1.getId(), savedAppPlat52.getId());
        assertEquals(4, savedAppPlat52.getDevices().size());

        assertEquals(false, savedDevice6.getActive());
        assertEquals(2, savedDevice6.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat61 = null, savedAppPlat62 = null;
        for (ApplicationPlatform ap : savedDevice6.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat61 = ap;
            } else if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat62 = ap;
            }
        }
        assertEquals(appPlat3.getId(), savedAppPlat61.getId());
        assertEquals(4, savedAppPlat61.getDevices().size());
        assertEquals(appPlat1.getId(), savedAppPlat62.getId());
        assertEquals(4, savedAppPlat62.getDevices().size());
    }

    /*
     * Insert 2 devices and then perform a save with 6 - 2 of which should be an
     * update while the others are inserts.
     */
    @Test
    public void testUpsertDevice() throws Exception
    {
        d1 = Device1.getDomainObject();
        d2 = Device2.getDomainObject();
        d3 = Device3.getDomainObject();
        d4 = Device4.getDomainObject();
        d5 = Device5.getDomainObject();
        d6 = Device6.getDomainObject();
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        ApplicationPlatform[] appPlats2 = { appPlat2 };
        ApplicationPlatform[] appPlats3 = { appPlat3 };
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats1)));
        d3.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d4.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));
        d5.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        d6.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats3)));
        TestDataHelper.createDevice(d1);
        TestDataHelper.createDevice(d2);

        d1.setActive(false);
        d1.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));

        d2.setActive(false);
        d2.setApplicationPlatforms(new HashSet<ApplicationPlatform>(Arrays.asList(appPlats2)));

        Device[] devices = { d1, d2, d3, d4, d5, d6 };
        List<Device> savedDevices = deviceDAO.saveDevice(devices);
        assertEquals(devices.length, savedDevices.size());
        Device savedDevice1 = null, savedDevice2 = null, savedDevice3 = null, savedDevice4 = null, savedDevice5 = null, savedDevice6 = null;
        for (Device d : savedDevices) {
            if (d.getCode().equals(Device1.CODE)) {
                savedDevice1 = d;
            } else if (d.getCode().equals(Device2.CODE)) {
                savedDevice2 = d;
            } else if (d.getCode().equals(Device3.CODE)) {
                savedDevice3 = d;
            } else if (d.getCode().equals(Device4.CODE)) {
                savedDevice4 = d;
            } else if (d.getCode().equals(Device5.CODE)) {
                savedDevice5 = d;
            } else if (d.getCode().equals(Device6.CODE)) {
                savedDevice6 = d;
            }
        }
        assertEquals(false, savedDevice1.getActive());
        assertEquals(2, savedDevice1.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat11 = null, savedAppPlat12 = null;
        for (ApplicationPlatform ap : savedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat11 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat12 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat11.getId());
        assertEquals(2, savedAppPlat11.getDevices().size());
        assertEquals(appPlat2.getId(), savedAppPlat12.getId());
        assertEquals(4, savedAppPlat12.getDevices().size());

        assertEquals(false, savedDevice2.getActive());
        assertEquals(2, savedDevice2.getApplicationPlatforms().size());
        ApplicationPlatform savedAppPlat21 = null, savedAppPlat22 = null;
        for (ApplicationPlatform ap : savedDevice1.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat1.getId())) {
                savedAppPlat21 = ap;
            } else if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat22 = ap;
            }
        }
        assertEquals(appPlat1.getId(), savedAppPlat21.getId());
        assertEquals(2, savedAppPlat21.getDevices().size());
        assertEquals(appPlat2.getId(), savedAppPlat22.getId());
        assertEquals(4, savedAppPlat22.getDevices().size());

        assertEquals(true, savedDevice3.getActive());
        assertEquals(appPlat2.getId(), savedDevice3.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat31 = null;
        for (ApplicationPlatform ap : savedDevice3.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat31 = ap;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlat31.getId());
        assertEquals(4, savedAppPlat31.getDevices().size());

        assertEquals(true, savedDevice4.getActive());
        assertEquals(appPlat2.getId(), savedDevice4.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat41 = null;
        for (ApplicationPlatform ap : savedDevice4.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat2.getId())) {
                savedAppPlat41 = ap;
            }
        }
        assertEquals(appPlat2.getId(), savedAppPlat41.getId());
        assertEquals(4, savedAppPlat41.getDevices().size());

        assertEquals(true, savedDevice5.getActive());
        assertEquals(appPlat3.getId(), savedDevice5.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat51 = null;
        for (ApplicationPlatform ap : savedDevice5.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat51 = ap;
            }
        }
        assertEquals(appPlat3.getId(), savedAppPlat51.getId());
        assertEquals(2, savedAppPlat51.getDevices().size());

        assertEquals(true, savedDevice6.getActive());
        assertEquals(appPlat3.getId(), savedDevice6.getApplicationPlatformId());
        ApplicationPlatform savedAppPlat61 = null;
        for (ApplicationPlatform ap : savedDevice6.getApplicationPlatforms()) {
            if (ap.getId().equals(appPlat3.getId())) {
                savedAppPlat61 = ap;
            }
        }
        assertEquals(appPlat3.getId(), savedAppPlat61.getId());
        assertEquals(2, savedAppPlat61.getDevices().size());
    }
}
