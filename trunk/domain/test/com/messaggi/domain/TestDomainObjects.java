package com.messaggi.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDomainObjects
{
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testApplication()
    {
        Application a1 = new Application();
        Assert.assertEquals(0, a1.getId());
        Assert.assertEquals(false, a1.isActive());
        Assert.assertEquals(0, a1.getApplicationPlatforms().size());
        Assert.assertNull(a1.getUserApplication());

        long id = 123;
        boolean active = true;
        Application a2 = new Application(id, active);
        Assert.assertEquals(id, a2.getId());
        Assert.assertEquals(active, a2.isActive());
        Assert.assertEquals(0, a2.getApplicationPlatforms().size());
        Assert.assertNull(a2.getUserApplication());

        Set<ApplicationPlatform> aps = new HashSet<>();
        ApplicationPlatform ap = new ApplicationPlatform();
        aps.add(ap);
        UserApplication up = new UserApplication();
        Application a3 = new Application(id, active, aps, up);
        Assert.assertEquals(id, a3.getId());
        Assert.assertEquals(active, a3.isActive());
        Assert.assertEquals(1, a3.getApplicationPlatforms().size());
        Assert.assertSame(ap, a3.getApplicationPlatforms().toArray()[0]);
        Assert.assertSame(up, a3.getUserApplication());
    }

    @Test
    public void testApplicationPlatform()
    {
        ApplicationPlatform ap1 = new ApplicationPlatform();
        Assert.assertEquals(0, ap1.getId());
        Assert.assertNull(ap1.getPlatform());
        Assert.assertNull(ap1.getApplication());
        Assert.assertNull(ap1.getToken());

        long id = 123;
        Platform p = new Platform();
        Application a = new Application();
        UUID t = UUID.randomUUID();
        ApplicationPlatform ap2 = new ApplicationPlatform(id, p, a, t);
        Assert.assertEquals(id, ap2.getId());
        Assert.assertSame(p, ap2.getPlatform());
        Assert.assertSame(a, ap2.getApplication());
        Assert.assertSame(t, ap2.getToken());
        Assert.assertEquals(0, ap2.getApplicationPlatformMsgLogs().size());
        Assert.assertEquals(0, ap2.getApplicationPlatformAttributes().size());

        ApplicationPlatformMsgLog apml = new ApplicationPlatformMsgLog();
        Set<ApplicationPlatformMsgLog> apmls = new HashSet<> ();
        apmls.add(apml);
        ApplicationPlatformAttribute apm = new ApplicationPlatformAttribute();
        Set<ApplicationPlatformAttribute> apms = new HashSet<> ();
        apms.add(apm);
        ApplicationPlatform ap3 = new ApplicationPlatform(id, p, a, t, apmls, apms);
        Assert.assertEquals(id, ap3.getId());
        Assert.assertSame(p, ap3.getPlatform());
        Assert.assertSame(a, ap3.getApplication());
        Assert.assertSame(t, ap3.getToken());
        Assert.assertEquals(1, ap3.getApplicationPlatformMsgLogs().size());
        Assert.assertSame(apml, ap3.getApplicationPlatformMsgLogs().toArray()[0]);
        Assert.assertEquals(1, ap3.getApplicationPlatformAttributes().size());
        Assert.assertSame(apm, ap3.getApplicationPlatformAttributes().toArray()[0]);
    }

    @Test
    public void testApplicationPlatformAttribute()
    {
    }

    @Test
    public void testApplicationPlatformDevice()
    {
    }

    @Test
    public void testApplicationPlatformKey()
    {
        ApplicationPlatformKey apk1 = new ApplicationPlatformKey();
        Assert.assertNull(apk1.getKey());
        Assert.assertNull(apk1.getDescription());
        Assert.assertEquals(0, apk1.getApplicationPlatformAttributes().size());

        String key = "BUNDLE_ID";
        String description = "Apple Application Bundle Identifier";
        ApplicationPlatformKey apk2 = new ApplicationPlatformKey(key, description);
        Assert.assertEquals(key, apk2.getKey());
        Assert.assertEquals(description, apk2.getDescription());
        Assert.assertEquals(0, apk2.getApplicationPlatformAttributes().size());

        Set<DeviceAttribute> das = new HashSet<>();
        DeviceAttribute da = new DeviceAttribute();
        das.add(da);
        ApplicationPlatformKey apk3 = new ApplicationPlatformKey(key, description, das);
        Assert.assertEquals(key, apk3.getKey());
        Assert.assertEquals(description, apk3.getDescription());
        Assert.assertEquals(1, apk3.getApplicationPlatformAttributes().size());
        Assert.assertSame(da, apk3.getApplicationPlatformAttributes().toArray()[0]);
    }

    @Test
    public void testApplicationPlatformMsgLog()
    {
    }

    @Test
    public void testDevice()
    {
        Device d1 = new Device();
        Assert.assertEquals(0, d1.getId());
        Assert.assertEquals(false, d1.isActive());
        Assert.assertEquals(0, d1.getDeviceAttributes().size());

        long id = 123;
        boolean active = true;
        Device d2 = new Device(id, active);
        Assert.assertEquals(id, d2.getId());
        Assert.assertEquals(active, d2.isActive());
        Assert.assertEquals(0, d2.getDeviceAttributes().size());

        Set<DeviceAttribute> das = new HashSet<>();
        DeviceAttribute da = new DeviceAttribute();
        das.add(da);
        Device d3 = new Device(id, active, das);
        Assert.assertEquals(id, d3.getId());
        Assert.assertEquals(active, d3.isActive());
        Assert.assertEquals(1, d3.getDeviceAttributes().size());
        Assert.assertSame(da, d3.getDeviceAttributes().toArray()[0]);
    }

    @Test
    public void testDeviceAttribute()
    {
        /*
         * DeviceAttribute da1 = new DeviceAttribute(); Assert.assertEquals(0,
         * da1.getId()); Assert.assertEquals(false, da1.isActive());
         * Assert.assertEquals(0, da1.getDeviceAttributes().size());
         * 
         * long id = 123; boolean active = true; DeviceAttribute da2 = new
         * DeviceAttribute(id, active); Assert.assertEquals(serviceName,
         * p2.getServiceName()); Assert.assertEquals(0,
         * da2.getDeviceAttributes().size());
         * 
         * Set<DeviceAttribute> das = new HashSet<>(); DeviceAttribute da = new
         * DeviceAttribute(); das.add(da); DeviceAttribute da3 = new
         * DeviceAttribute(id, active, das); Assert.assertEquals(id,
         * da3.getId()); Assert.assertEquals(active, da3.isActive());
         * Assert.assertEquals(1, da3.getDeviceAttributes().size());
         * Assert.assertSame(da, da3.getDeviceAttributes().toArray()[0]);
         */
    }

    @Test
    public void testDeviceKey()
    {
        DeviceKey dk1 = new DeviceKey();
        Assert.assertNull(dk1.getKey());
        Assert.assertNull(dk1.getDescription());
        Assert.assertEquals(0, dk1.getDeviceAttributes().size());

        String key = "BUNDLE_ID";
        String description = "Apple Application Bundle Identifier";
        DeviceKey dk2 = new DeviceKey(key, description);
        Assert.assertEquals(key, dk2.getKey());
        Assert.assertEquals(description, dk2.getDescription());
        Assert.assertEquals(0, dk2.getDeviceAttributes().size());

        Set<DeviceAttribute> das = new HashSet<>();
        DeviceAttribute da = new DeviceAttribute();
        das.add(da);
        DeviceKey dk3 = new DeviceKey(key, description, das);
        Assert.assertEquals(key, dk3.getKey());
        Assert.assertEquals(description, dk3.getDescription());
        Assert.assertEquals(1, dk3.getDeviceAttributes().size());
        Assert.assertSame(da, dk3.getDeviceAttributes().toArray()[0]);
    }

    @Test
    public void testPlatform()
    {
        Platform p1 = new Platform();
        Assert.assertEquals(0, p1.getId());
        Assert.assertNull(p1.getName());
        Assert.assertNull(p1.getServiceName());
        Assert.assertEquals(false, p1.isActive());
        Assert.assertEquals(0, p1.getApplicationPlatforms().size());

        long id = 123;
        boolean active = true;
        String name = "iOS";
        String serviceName = "Apple Push Notification Service";
        Platform p2 = new Platform(id, name, serviceName, active);
        Assert.assertEquals(id, p2.getId());
        Assert.assertEquals(name, p2.getName());
        Assert.assertEquals(serviceName, p2.getServiceName());
        Assert.assertEquals(active, p2.isActive());
        Assert.assertEquals(0, p2.getApplicationPlatforms().size());

        Set<ApplicationPlatform> aps = new HashSet<>();
        ApplicationPlatform ap = new ApplicationPlatform();
        aps.add(ap);
        Platform p3 = new Platform(id, name, serviceName, active, aps);
        Assert.assertEquals(id, p3.getId());
        Assert.assertEquals(name, p3.getName());
        Assert.assertEquals(serviceName, p3.getServiceName());
        Assert.assertEquals(active, p3.isActive());
        Assert.assertEquals(1, p3.getApplicationPlatforms().size());
        Assert.assertSame(ap, p3.getApplicationPlatforms().toArray()[0]);
    }

    @Test
    public void testUser()
    {
        User u1 = new User();
        Assert.assertEquals(0, u1.getId());
        Assert.assertNull(u1.getName());
        Assert.assertNull(u1.getEmail());
        Assert.assertNull(u1.getPhone());
        Assert.assertNull(u1.getPhoneParsed());
        Assert.assertNull(u1.getPassword());
        Assert.assertNull(u1.getLocale());
        Assert.assertEquals(false, u1.isActive());
        Assert.assertEquals(0, u1.getUserApplications().size());

        long id = 123;
        boolean active = true;
        String name = "Justin C. Wolfe";
        String email = "Justin@Messaggi.com";
        String phone = "617-549-1234";
        String phoneParsed = "6175491234";
        String password = "123abXX!!";
        String locale = "en-US";
        User u2 = new User(id, name, email, phone, phoneParsed, password, locale, active);
        Assert.assertEquals(id, u2.getId());
        Assert.assertEquals(name, u2.getName());
        Assert.assertEquals(email, u2.getEmail());
        Assert.assertEquals(phone, u2.getPhone());
        Assert.assertEquals(phoneParsed, u2.getPhoneParsed());
        Assert.assertEquals(password, u2.getPassword());
        Assert.assertEquals(locale, u2.getLocale());
        Assert.assertEquals(active, u2.isActive());
        Assert.assertEquals(0, u2.getUserApplications().size());

        Set<UserApplication> uas = new HashSet<>();
        UserApplication ua = new UserApplication();
        uas.add(ua);
        User u3 = new User(id, name, email, phone, phoneParsed, password, locale, active, uas);
        Assert.assertEquals(id, u3.getId());
        Assert.assertEquals(name, u3.getName());
        Assert.assertEquals(email, u3.getEmail());
        Assert.assertEquals(phone, u3.getPhone());
        Assert.assertEquals(phoneParsed, u3.getPhoneParsed());
        Assert.assertEquals(password, u3.getPassword());
        Assert.assertEquals(locale, u3.getLocale());
        Assert.assertEquals(active, u3.isActive());
        Assert.assertEquals(1, u3.getUserApplications().size());
        Assert.assertSame(ua, u3.getUserApplications().toArray()[0]);
    }

    @Test
    public void testUserApplication()
    {
        UserApplication ua1 = new UserApplication();
        Assert.assertEquals(0, ua1.getApplicationId());
        Assert.assertNull(ua1.getUser());

        long applicationId = 123;
        User user = new User();
        UserApplication ua2 = new UserApplication(applicationId, user);
        Assert.assertEquals(applicationId, ua2.getApplicationId());
        Assert.assertSame(user, ua2.getUser());
    }
}

