package com.messaggi.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.junit.categories.LogicTests;

@Category(LogicTests.class)
public class TestDomainObjects extends MessaggiTestCase
{
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
    public void testApplicationPlatformAttributeId()
    {
        ApplicationPlatformAttributeId apaid1 = new ApplicationPlatformAttributeId();
        Assert.assertNull(apaid1.getApplicationPlatform());
        Assert.assertNull(apaid1.getApplicationPlatformKey());

        ApplicationPlatform ap1 = new ApplicationPlatform();
        ApplicationPlatformKey apk1 = new ApplicationPlatformKey();
        ApplicationPlatformAttributeId apaid2 = new ApplicationPlatformAttributeId(ap1, apk1);
        Assert.assertEquals(ap1, apaid2.getApplicationPlatform());
        Assert.assertEquals(apk1, apaid2.getApplicationPlatformKey());
        Assert.assertNotSame(apaid1, apaid2);
        Assert.assertNotEquals(apaid1, apaid2);

        ApplicationPlatformAttributeId apaid3 = new ApplicationPlatformAttributeId(ap1, apk1);
        Assert.assertEquals(ap1, apaid3.getApplicationPlatform());
        Assert.assertEquals(apk1, apaid3.getApplicationPlatformKey());
        Assert.assertNotSame(apaid2, apaid3);
        Assert.assertEquals(apaid2, apaid3);
        Assert.assertEquals(apaid2.hashCode(), apaid3.hashCode());

        ApplicationPlatform ap2 = new ApplicationPlatform();
        ApplicationPlatformKey apk2 = new ApplicationPlatformKey();
        ApplicationPlatformAttributeId apaid4 = new ApplicationPlatformAttributeId(ap2, apk2);
        Assert.assertEquals(ap2, apaid4.getApplicationPlatform());
        Assert.assertEquals(apk2, apaid4.getApplicationPlatformKey());
        Assert.assertNotSame(apaid3, apaid4);
        Assert.assertNotEquals(apaid3, apaid4);
        Assert.assertNotEquals(apaid3.hashCode(), apaid4.hashCode());
    }

    @Test
    public void testApplicationPlatformAttribute()
    {
        ApplicationPlatformAttribute apa1 = new ApplicationPlatformAttribute();
        Assert.assertNull(apa1.getId());
        Assert.assertNull(apa1.getValue());

        ApplicationPlatformAttributeId apaid1 = new ApplicationPlatformAttributeId();
        String value = "some attribute value";
        ApplicationPlatformAttribute apa2 = new ApplicationPlatformAttribute(apaid1, value);
        Assert.assertEquals(apaid1, apa2.getId());
        Assert.assertEquals(value, apa2.getValue());

        ApplicationPlatform ap = new ApplicationPlatform();
        ApplicationPlatformKey apk = new ApplicationPlatformKey();
        ApplicationPlatformAttributeId apaid2 = new ApplicationPlatformAttributeId(ap, apk);
        ApplicationPlatformAttribute apa3 = new ApplicationPlatformAttribute(apaid2, value);
        Assert.assertEquals(apaid2, apa3.getId());
        Assert.assertEquals(ap, apa3.getId().getApplicationPlatform());
        Assert.assertEquals(apk, apa3.getId().getApplicationPlatformKey());
        Assert.assertEquals(value, apa3.getValue());
    }

    @Test
    public void testApplicationPlatformDeviceId()
    {
        ApplicationPlatformDeviceId apdid1 = new ApplicationPlatformDeviceId();
        Assert.assertEquals(0, apdid1.getApplicationPlatformId());
        Assert.assertEquals(0, apdid1.getDeviceId());

        long deviceId1 = 123;
        long platformId1 = 456;
        ApplicationPlatformDeviceId apdid2 = new ApplicationPlatformDeviceId(platformId1, deviceId1);
        Assert.assertEquals(platformId1, apdid2.getApplicationPlatformId());
        Assert.assertEquals(deviceId1, apdid2.getDeviceId());
        Assert.assertNotSame(apdid1, apdid2);
        Assert.assertNotEquals(apdid1, apdid2);

        ApplicationPlatformDeviceId apdid3 = new ApplicationPlatformDeviceId(platformId1, deviceId1);
        Assert.assertEquals(platformId1, apdid3.getApplicationPlatformId());
        Assert.assertEquals(deviceId1, apdid3.getDeviceId());
        Assert.assertNotSame(apdid2, apdid3);
        Assert.assertEquals(apdid2, apdid3);
        Assert.assertEquals(apdid2.hashCode(), apdid3.hashCode());

        long deviceId2 = 789;
        long platformId2 = 012;
        ApplicationPlatformDeviceId apdid4 = new ApplicationPlatformDeviceId(platformId2, deviceId2);
        Assert.assertEquals(platformId2, apdid4.getApplicationPlatformId());
        Assert.assertEquals(deviceId2, apdid4.getDeviceId());
        Assert.assertNotSame(apdid3, apdid4);
        Assert.assertNotEquals(apdid3, apdid4);
        Assert.assertNotEquals(apdid3.hashCode(), apdid4.hashCode());
    }

    @Test
    public void testApplicationPlatformDevice()
    {
        ApplicationPlatformDevice apd1 = new ApplicationPlatformDevice();
        Assert.assertNull(apd1.getId());

        ApplicationPlatformDeviceId apdid1 = new ApplicationPlatformDeviceId();
        ApplicationPlatformDevice apd2 = new ApplicationPlatformDevice(apdid1);
        Assert.assertEquals(apdid1, apd2.getId());
        Assert.assertEquals(0, apd2.getId().getApplicationPlatformId());
        Assert.assertEquals(0, apd2.getId().getDeviceId());

        long deviceId1 = 123;
        long platformId1 = 456;
        ApplicationPlatformDeviceId apdid2 = new ApplicationPlatformDeviceId(platformId1, deviceId1);
        ApplicationPlatformDevice apd3 = new ApplicationPlatformDevice(apdid2);
        Assert.assertEquals(apdid2, apd3.getId());
        Assert.assertEquals(platformId1, apd3.getId().getApplicationPlatformId());
        Assert.assertEquals(deviceId1, apd3.getId().getDeviceId());
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

        Set<ApplicationPlatformAttribute> apas = new HashSet<>();
        ApplicationPlatformAttribute apa = new ApplicationPlatformAttribute();
        apas.add(apa);
        ApplicationPlatformKey apk3 = new ApplicationPlatformKey(key, description, apas);
        Assert.assertEquals(key, apk3.getKey());
        Assert.assertEquals(description, apk3.getDescription());
        Assert.assertEquals(1, apk3.getApplicationPlatformAttributes().size());
        Assert.assertSame(apa, apk3.getApplicationPlatformAttributes().toArray()[0]);
    }

    @Test
    public void testApplicationPlatformMsgLog()
    {
        ApplicationPlatformMsgLog apml1 = new ApplicationPlatformMsgLog();
        Assert.assertEquals(0, apml1.getId());
        Assert.assertNull(apml1.getApplicationPlatform());
        Assert.assertNull(apml1.getDate());
        Assert.assertEquals(0, apml1.getMsgCount());

        long id = 123;
        ApplicationPlatform ap = new ApplicationPlatform();
        Date date = new Date(System.currentTimeMillis());
        int msgCount = 100;
        ApplicationPlatformMsgLog apml2 = new ApplicationPlatformMsgLog(id, ap, date, msgCount);
        Assert.assertEquals(id, apml2.getId());
        Assert.assertSame(ap, apml2.getApplicationPlatform());
        Assert.assertSame(date, apml2.getDate());
        Assert.assertEquals(msgCount, apml2.getMsgCount());
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
    public void testDeviceAttributeId()
    {
        DeviceAttributeId daid1 = new DeviceAttributeId();
        Assert.assertNull(daid1.getDevice());
        Assert.assertNull(daid1.getDeviceKey());

        Device d1 = new Device();
        DeviceKey dk1 = new DeviceKey();
        DeviceAttributeId daid2 = new DeviceAttributeId(d1, dk1);
        Assert.assertEquals(d1, daid2.getDevice());
        Assert.assertEquals(dk1, daid2.getDeviceKey());
        Assert.assertNotSame(daid1, daid2);
        Assert.assertNotEquals(daid1, daid2);

        DeviceAttributeId daid3 = new DeviceAttributeId(d1, dk1);
        Assert.assertEquals(d1, daid3.getDevice());
        Assert.assertEquals(dk1, daid3.getDeviceKey());
        Assert.assertNotSame(daid2, daid3);
        Assert.assertEquals(daid2, daid3);
        Assert.assertEquals(daid2.hashCode(), daid3.hashCode());

        Device d2 = new Device();
        DeviceKey dk2 = new DeviceKey();
        DeviceAttributeId daid4 = new DeviceAttributeId(d2, dk2);
        Assert.assertEquals(d2, daid4.getDevice());
        Assert.assertEquals(dk2, daid4.getDeviceKey());
        Assert.assertNotSame(daid3, daid4);
        Assert.assertNotEquals(daid3, daid4);
        Assert.assertNotEquals(daid3.hashCode(), daid4.hashCode());
    }

    @Test
    public void testDeviceAttribute()
    {
        DeviceAttribute da1 = new DeviceAttribute();
        Assert.assertNull(da1.getId());
        Assert.assertNull(da1.getValue());

        DeviceAttributeId daid1 = new DeviceAttributeId();
        String value = "some attribute value";
        DeviceAttribute da2 = new DeviceAttribute(daid1, value);
        Assert.assertEquals(daid1, da2.getId());
        Assert.assertEquals(value, da2.getValue());

        Device d = new Device();
        DeviceKey dk = new DeviceKey();
        DeviceAttributeId daid2 = new DeviceAttributeId(d, dk);
        DeviceAttribute da3 = new DeviceAttribute(daid2, value);
        Assert.assertEquals(daid2, da3.getId());
        Assert.assertEquals(d, da3.getId().getDevice());
        Assert.assertEquals(dk, da3.getId().getDeviceKey());
        Assert.assertEquals(value, da3.getValue());
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

