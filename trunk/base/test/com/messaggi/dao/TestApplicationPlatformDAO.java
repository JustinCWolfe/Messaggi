package com.messaggi.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.TestDataHelper;
import com.messaggi.TestDataHelper.Application1;
import com.messaggi.TestDataHelper.Application2;
import com.messaggi.TestDataHelper.ApplicationPlatform1;
import com.messaggi.TestDataHelper.ApplicationPlatform2;
import com.messaggi.TestDataHelper.ApplicationPlatform3;
import com.messaggi.TestDataHelper.ApplicationPlatform4;
import com.messaggi.TestDataHelper.User1;
import com.messaggi.domain.Application;
import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.User;
import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.util.EncodeHelper;

public class TestApplicationPlatformDAO extends MessaggiTestCase
{
    private Application app1;

    private Application app2;

    private ApplicationPlatform appPlat1;

    private ApplicationPlatform appPlat2;

    private ApplicationPlatform appPlat3;

    private ApplicationPlatform appPlat4;

    private ApplicationPlatformDAO applicationPlatformDAO;

    private User user1;

    @Override
    @Before
    public void setUp() throws Exception
    {
        user1 = User1.getDomainObject();
        TestDataHelper.createUser(user1);
        app1 = Application1.getDomainObject();
        app2 = Application2.getDomainObject();
        app1.setUser(user1);
        app2.setUser(user1);
        TestDataHelper.createApplication(app1);
        TestDataHelper.createApplication(app2);
        applicationPlatformDAO = new ApplicationPlatformDAO();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        TestDataHelper.deleteApplicationPlatform(appPlat1);
        TestDataHelper.deleteApplicationPlatform(appPlat2);
        TestDataHelper.deleteApplicationPlatform(appPlat3);
        TestDataHelper.deleteApplicationPlatform(appPlat4);
        TestDataHelper.deleteApplication(app1);
        TestDataHelper.deleteApplication(app2);
        TestDataHelper.deleteUser(user1);
    }

    @Test
    public void testGetApplicationPlatformById() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat1.getId());
        queryAppPlat1.setToken(null);
        queryAppPlat2.setId(appPlat2.getId());
        queryAppPlat2.setToken(null);
        queryAppPlat3.setId(appPlat3.getId());
        queryAppPlat3.setToken(null);
        queryAppPlat4.setId(appPlat4.getId());
        queryAppPlat4.setToken(null);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        ApplicationPlatform retrievedAppPlat1 = null, retrievedAppPlat2 = null, retrievedAppPlat3 = null, retrievedAppPlat4 = null;
        for (ApplicationPlatform appPlat : retrievedAppPlats) {
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
        assertEquals(3, retrievedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat1.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat1.getToken());
        assertThat(retrievedAppPlat1.getExternalServiceToken(), equalTo(appPlat1.getExternalServiceToken()));
        assertThat(retrievedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(appPlat1.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat1.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(retrievedAppPlat1.getExternalServicePasswordAsBinary(), equalTo(appPlat1.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat2.getId(), retrievedAppPlat2.getId());
        assertEquals(app1.getId(), retrievedAppPlat2.getApplication().getId());
        assertEquals(3, retrievedAppPlat2.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat2.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat2.getToken());
        assertThat(retrievedAppPlat2.getExternalServiceToken(), equalTo(appPlat2.getExternalServiceToken()));
        assertThat(retrievedAppPlat2.getExternalServiceTokenAsBinary(),
                equalTo(appPlat2.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat2.getExternalServicePassword(), equalTo(appPlat2.getExternalServicePassword()));
        assertThat(retrievedAppPlat2.getExternalServicePasswordAsBinary(),
                equalTo(appPlat2.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat3.getId(), retrievedAppPlat3.getId());
        assertEquals(3, retrievedAppPlat3.getApplication().getApplicationPlatforms().size());
        assertEquals(app1.getId(), retrievedAppPlat3.getApplication().getId());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat3.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat3.getToken());
        assertThat(retrievedAppPlat3.getExternalServiceToken(), equalTo(appPlat3.getExternalServiceToken()));
        assertThat(retrievedAppPlat3.getExternalServiceTokenAsBinary(),
                equalTo(appPlat3.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat3.getExternalServicePassword(), equalTo(appPlat3.getExternalServicePassword()));
        assertThat(retrievedAppPlat3.getExternalServicePasswordAsBinary(),
                equalTo(appPlat3.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat4.getId(), retrievedAppPlat4.getId());
        assertEquals(1, retrievedAppPlat4.getApplication().getApplicationPlatforms().size());
        assertEquals(app2.getId(), retrievedAppPlat4.getApplication().getId());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat4.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat4.getToken());
        assertThat(retrievedAppPlat4.getExternalServiceToken(), equalTo(appPlat4.getExternalServiceToken()));
        assertThat(retrievedAppPlat4.getExternalServiceTokenAsBinary(),
                equalTo(appPlat4.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat4.getExternalServicePassword(), equalTo(appPlat4.getExternalServicePassword()));
        assertThat(retrievedAppPlat4.getExternalServicePasswordAsBinary(),
                equalTo(appPlat4.getExternalServicePasswordAsBinary()));
    }

    @Test
    public void testGetApplicationPlatformByIdAndIncorrectApplicationIdAndToken() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat1.getId());
        queryAppPlat1.setApplication(app2);
        queryAppPlat1.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat2.setId(appPlat2.getId());
        queryAppPlat2.setApplication(app2);
        queryAppPlat2.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat3.setId(appPlat3.getId());
        queryAppPlat3.setApplication(app2);
        queryAppPlat3.setToken(ApplicationPlatform4.TOKEN);
        queryAppPlat4.setId(appPlat4.getId());
        queryAppPlat4.setApplication(app1);
        queryAppPlat4.setToken(ApplicationPlatform1.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByIdAndApplicationIdAndIncorrectToken() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat1.getId());
        queryAppPlat1.setApplication(app1);
        queryAppPlat1.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat2.setId(appPlat2.getId());
        queryAppPlat2.setApplication(app1);
        queryAppPlat2.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat3.setId(appPlat3.getId());
        queryAppPlat3.setApplication(app1);
        queryAppPlat3.setToken(ApplicationPlatform4.TOKEN);
        queryAppPlat4.setId(appPlat4.getId());
        queryAppPlat4.setApplication(app2);
        queryAppPlat4.setToken(ApplicationPlatform1.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByIdAndTokenAndIncorrectApplicationId() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat1.getId());
        queryAppPlat1.setApplication(app2);
        queryAppPlat1.setToken(ApplicationPlatform1.TOKEN);
        queryAppPlat2.setId(appPlat2.getId());
        queryAppPlat2.setApplication(app2);
        queryAppPlat2.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat3.setId(appPlat3.getId());
        queryAppPlat3.setApplication(app2);
        queryAppPlat3.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat4.setId(appPlat4.getId());
        queryAppPlat4.setApplication(app1);
        queryAppPlat4.setToken(ApplicationPlatform4.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByToken() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        ApplicationPlatform retrievedAppPlat1 = null, retrievedAppPlat2 = null, retrievedAppPlat3 = null, retrievedAppPlat4 = null;
        for (ApplicationPlatform appPlat : retrievedAppPlats) {
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
        assertEquals(3, retrievedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat1.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat1.getToken());
        assertThat(retrievedAppPlat1.getExternalServiceToken(), equalTo(appPlat1.getExternalServiceToken()));
        assertThat(retrievedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(appPlat1.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat1.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(retrievedAppPlat1.getExternalServicePasswordAsBinary(), equalTo(appPlat1.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat2.getId(), retrievedAppPlat2.getId());
        assertEquals(app1.getId(), retrievedAppPlat2.getApplication().getId());
        assertEquals(3, retrievedAppPlat2.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat2.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat2.getToken());
        assertThat(retrievedAppPlat2.getExternalServiceToken(), equalTo(appPlat2.getExternalServiceToken()));
        assertThat(retrievedAppPlat2.getExternalServiceTokenAsBinary(),
                equalTo(appPlat2.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat2.getExternalServicePassword(), equalTo(appPlat2.getExternalServicePassword()));
        assertThat(retrievedAppPlat2.getExternalServicePasswordAsBinary(),
                equalTo(appPlat2.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat3.getId(), retrievedAppPlat3.getId());
        assertEquals(3, retrievedAppPlat3.getApplication().getApplicationPlatforms().size());
        assertEquals(app1.getId(), retrievedAppPlat3.getApplication().getId());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat3.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat3.getToken());
        assertThat(retrievedAppPlat3.getExternalServiceToken(), equalTo(appPlat3.getExternalServiceToken()));
        assertThat(retrievedAppPlat3.getExternalServiceTokenAsBinary(),
                equalTo(appPlat3.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat3.getExternalServicePassword(), equalTo(appPlat3.getExternalServicePassword()));
        assertThat(retrievedAppPlat3.getExternalServicePasswordAsBinary(),
                equalTo(appPlat3.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat4.getId(), retrievedAppPlat4.getId());
        assertEquals(1, retrievedAppPlat4.getApplication().getApplicationPlatforms().size());
        assertEquals(app2.getId(), retrievedAppPlat4.getApplication().getId());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat4.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat4.getToken());
        assertThat(retrievedAppPlat4.getExternalServiceToken(), equalTo(appPlat4.getExternalServiceToken()));
        assertThat(retrievedAppPlat4.getExternalServiceTokenAsBinary(),
                equalTo(appPlat4.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat4.getExternalServicePassword(), equalTo(appPlat4.getExternalServicePassword()));
        assertThat(retrievedAppPlat4.getExternalServicePasswordAsBinary(),
                equalTo(appPlat4.getExternalServicePasswordAsBinary()));
    }

    @Test
    public void testGetApplicationPlatformByTokenAndIncorrectIdAndApplicationId() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat2.getId());
        queryAppPlat1.setApplication(app2);
        queryAppPlat1.setToken(ApplicationPlatform1.TOKEN);
        queryAppPlat2.setId(appPlat3.getId());
        queryAppPlat2.setApplication(app2);
        queryAppPlat2.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat3.setId(appPlat4.getId());
        queryAppPlat3.setApplication(app2);
        queryAppPlat3.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat4.setId(appPlat1.getId());
        queryAppPlat4.setApplication(app1);
        queryAppPlat4.setToken(ApplicationPlatform4.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByTokenAndApplicationIdAndIncorrectId() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat2.getId());
        queryAppPlat1.setApplication(app1);
        queryAppPlat1.setToken(ApplicationPlatform1.TOKEN);
        queryAppPlat2.setId(appPlat3.getId());
        queryAppPlat2.setApplication(app1);
        queryAppPlat2.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat3.setId(appPlat4.getId());
        queryAppPlat3.setApplication(app1);
        queryAppPlat3.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat4.setId(appPlat1.getId());
        queryAppPlat4.setApplication(app2);
        queryAppPlat4.setToken(ApplicationPlatform4.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByApplicationId() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setApplication(app1);
        queryAppPlat1.setToken(null);
        queryAppPlat2.setApplication(app1);
        queryAppPlat2.setToken(null);
        queryAppPlat3.setApplication(app1);
        queryAppPlat3.setToken(null);
        queryAppPlat4.setApplication(app2);
        queryAppPlat4.setToken(null);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        ApplicationPlatform retrievedAppPlat1 = null, retrievedAppPlat2 = null, retrievedAppPlat3 = null, retrievedAppPlat4 = null;
        for (ApplicationPlatform appPlat : retrievedAppPlats) {
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
        assertEquals(3, retrievedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat1.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat1.getToken());
        assertThat(retrievedAppPlat1.getExternalServiceToken(), equalTo(appPlat1.getExternalServiceToken()));
        assertThat(retrievedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(appPlat1.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat1.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(retrievedAppPlat1.getExternalServicePasswordAsBinary(), equalTo(appPlat1.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat2.getId(), retrievedAppPlat2.getId());
        assertEquals(appPlat2.getApplication().getId(), retrievedAppPlat2.getApplication().getId());
        assertEquals(3, retrievedAppPlat2.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat2.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat2.getToken());
        assertThat(retrievedAppPlat2.getExternalServiceToken(), equalTo(appPlat2.getExternalServiceToken()));
        assertThat(retrievedAppPlat2.getExternalServiceTokenAsBinary(),
                equalTo(appPlat2.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat2.getExternalServicePassword(), equalTo(appPlat2.getExternalServicePassword()));
        assertThat(retrievedAppPlat2.getExternalServicePasswordAsBinary(),
                equalTo(appPlat2.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat3.getId(), retrievedAppPlat3.getId());
        assertEquals(appPlat2.getApplication().getId(), retrievedAppPlat3.getApplication().getId());
        assertEquals(3, retrievedAppPlat3.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat3.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat3.getToken());
        assertThat(retrievedAppPlat3.getExternalServiceToken(), equalTo(appPlat3.getExternalServiceToken()));
        assertThat(retrievedAppPlat3.getExternalServiceTokenAsBinary(),
                equalTo(appPlat3.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat3.getExternalServicePassword(), equalTo(appPlat3.getExternalServicePassword()));
        assertThat(retrievedAppPlat3.getExternalServicePasswordAsBinary(),
                equalTo(appPlat3.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat4.getId(), retrievedAppPlat4.getId());
        assertEquals(appPlat4.getApplication().getId(), retrievedAppPlat4.getApplication().getId());
        assertEquals(1, retrievedAppPlat4.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat4.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat4.getToken());
        assertThat(retrievedAppPlat4.getExternalServiceToken(), equalTo(appPlat4.getExternalServiceToken()));
        assertThat(retrievedAppPlat4.getExternalServiceTokenAsBinary(),
                equalTo(appPlat4.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat4.getExternalServicePassword(), equalTo(appPlat4.getExternalServicePassword()));
        assertThat(retrievedAppPlat4.getExternalServicePasswordAsBinary(),
                equalTo(appPlat4.getExternalServicePasswordAsBinary()));
    }

    @Test
    public void testGetApplicationPlatformByApplicationIdAndIncorrectIdAndToken() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat2.getId());
        queryAppPlat1.setApplication(app1);
        queryAppPlat1.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat2.setId(appPlat3.getId());
        queryAppPlat2.setApplication(app1);
        queryAppPlat2.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat3.setId(appPlat4.getId());
        queryAppPlat3.setApplication(app1);
        queryAppPlat3.setToken(ApplicationPlatform4.TOKEN);
        queryAppPlat4.setId(appPlat1.getId());
        queryAppPlat4.setApplication(app2);
        queryAppPlat4.setToken(ApplicationPlatform1.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByApplicationIdAndTokenAndIncorrectId() throws Exception
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
        ApplicationPlatform queryAppPlat1 = ApplicationPlatform1.getDomainObject();
        ApplicationPlatform queryAppPlat2 = ApplicationPlatform2.getDomainObject();
        ApplicationPlatform queryAppPlat3 = ApplicationPlatform3.getDomainObject();
        ApplicationPlatform queryAppPlat4 = ApplicationPlatform4.getDomainObject();
        queryAppPlat1.setId(appPlat2.getId());
        queryAppPlat1.setApplication(app1);
        queryAppPlat1.setToken(ApplicationPlatform1.TOKEN);
        queryAppPlat2.setId(appPlat3.getId());
        queryAppPlat2.setApplication(app1);
        queryAppPlat2.setToken(ApplicationPlatform2.TOKEN);
        queryAppPlat3.setId(appPlat4.getId());
        queryAppPlat3.setApplication(app1);
        queryAppPlat3.setToken(ApplicationPlatform3.TOKEN);
        queryAppPlat4.setId(appPlat1.getId());
        queryAppPlat4.setApplication(app2);
        queryAppPlat4.setToken(ApplicationPlatform4.TOKEN);
        ApplicationPlatform[] appPlats = { queryAppPlat1, queryAppPlat1, queryAppPlat2, queryAppPlat2, queryAppPlat3,
                queryAppPlat3, queryAppPlat4, queryAppPlat4 };
        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getApplicationPlatform(appPlats);
        assertEquals(0, retrievedAppPlats.size());
    }

    @Test
    public void testGetApplicationPlatformByIdAndApplicationIdAndToken() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat1.setApplication(app1);
        ApplicationPlatform[] appPlats1 = { appPlat1 };
        List<ApplicationPlatform> retrievedAppPlats1 = applicationPlatformDAO.getApplicationPlatform(appPlats1);
        assertEquals(0, retrievedAppPlats1.size());

        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);
        ApplicationPlatform[] appPlats2 = { appPlat1, appPlat1, appPlat2, appPlat2, appPlat3, appPlat3, appPlat4,
                appPlat4 };
        List<ApplicationPlatform> retrievedAppPlats2 = applicationPlatformDAO.getApplicationPlatform(appPlats2);
        assertEquals(0, retrievedAppPlats2.size());

        TestDataHelper.createApplicationPlatform(appPlat1);
        List<ApplicationPlatform> retrievedAppPlats3 = applicationPlatformDAO.getApplicationPlatform(appPlats2);
        ApplicationPlatform retrievedAppPlat31 = retrievedAppPlats3.get(0);
        assertEquals(1, retrievedAppPlats3.size());
        assertEquals(appPlat1.getId(), retrievedAppPlat31.getId());
        assertEquals(appPlat1.getApplication().getId(), retrievedAppPlat31.getApplication().getId());
        assertEquals(1, retrievedAppPlat31.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat31.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat31.getToken());
        assertThat(retrievedAppPlat31.getExternalServiceToken(), equalTo(appPlat1.getExternalServiceToken()));
        assertThat(retrievedAppPlat31.getExternalServiceTokenAsBinary(),
                equalTo(appPlat1.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat31.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(retrievedAppPlat31.getExternalServicePasswordAsBinary(),
                equalTo(appPlat1.getExternalServicePasswordAsBinary()));

        TestDataHelper.createApplicationPlatform(appPlat2);
        TestDataHelper.createApplicationPlatform(appPlat3);
        TestDataHelper.createApplicationPlatform(appPlat4);
        List<ApplicationPlatform> retrievedAppPlats4 = applicationPlatformDAO.getApplicationPlatform(appPlats2);
        ApplicationPlatform retrievedAppPlat41 = null, retrievedAppPlat42 = null, retrievedAppPlat43 = null, retrievedAppPlat44 = null;
        for (ApplicationPlatform appPlat : retrievedAppPlats4) {
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
        assertEquals(3, retrievedAppPlat41.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat1.getPlatform(), retrievedAppPlat41.getPlatform());
        assertEquals(appPlat1.getToken(), retrievedAppPlat41.getToken());
        assertThat(retrievedAppPlat41.getExternalServiceToken(), equalTo(appPlat1.getExternalServiceToken()));
        assertThat(retrievedAppPlat41.getExternalServiceTokenAsBinary(),
                equalTo(appPlat1.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat41.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(retrievedAppPlat41.getExternalServicePasswordAsBinary(),
                equalTo(appPlat1.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat2.getId(), retrievedAppPlat42.getId());
        assertEquals(appPlat2.getApplication().getId(), retrievedAppPlat42.getApplication().getId());
        assertEquals(3, retrievedAppPlat42.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat2.getPlatform(), retrievedAppPlat42.getPlatform());
        assertEquals(appPlat2.getToken(), retrievedAppPlat42.getToken());
        assertThat(retrievedAppPlat42.getExternalServiceToken(), equalTo(appPlat2.getExternalServiceToken()));
        assertThat(retrievedAppPlat42.getExternalServiceTokenAsBinary(),
                equalTo(appPlat2.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat42.getExternalServicePassword(), equalTo(appPlat2.getExternalServicePassword()));
        assertThat(retrievedAppPlat42.getExternalServicePasswordAsBinary(),
                equalTo(appPlat2.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat3.getId(), retrievedAppPlat43.getId());
        assertEquals(appPlat3.getApplication().getId(), retrievedAppPlat43.getApplication().getId());
        assertEquals(3, retrievedAppPlat43.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat3.getPlatform(), retrievedAppPlat43.getPlatform());
        assertEquals(appPlat3.getToken(), retrievedAppPlat43.getToken());
        assertThat(retrievedAppPlat43.getExternalServiceToken(), equalTo(appPlat3.getExternalServiceToken()));
        assertThat(retrievedAppPlat43.getExternalServiceTokenAsBinary(),
                equalTo(appPlat3.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat43.getExternalServicePassword(), equalTo(appPlat3.getExternalServicePassword()));
        assertThat(retrievedAppPlat43.getExternalServicePasswordAsBinary(),
                equalTo(appPlat3.getExternalServicePasswordAsBinary()));
        assertEquals(appPlat4.getId(), retrievedAppPlat44.getId());
        assertEquals(appPlat4.getApplication().getId(), retrievedAppPlat44.getApplication().getId());
        assertEquals(1, retrievedAppPlat44.getApplication().getApplicationPlatforms().size());
        assertEquals(appPlat4.getPlatform(), retrievedAppPlat44.getPlatform());
        assertEquals(appPlat4.getToken(), retrievedAppPlat44.getToken());
        assertThat(retrievedAppPlat44.getExternalServiceToken(), equalTo(appPlat4.getExternalServiceToken()));
        assertThat(retrievedAppPlat44.getExternalServiceTokenAsBinary(),
                equalTo(appPlat4.getExternalServiceTokenAsBinary()));
        assertThat(retrievedAppPlat44.getExternalServicePassword(), equalTo(appPlat4.getExternalServicePassword()));
        assertThat(retrievedAppPlat44.getExternalServicePasswordAsBinary(),
                equalTo(appPlat4.getExternalServicePasswordAsBinary()));
    }

    @Test
    public void testGetAllApplicationPlatformIdsAndTokens() throws Exception
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

        List<ApplicationPlatform> retrievedAppPlats = applicationPlatformDAO.getAllApplicationPlatformIdsAndTokens();
        assertTrue(retrievedAppPlats.size() >= 4);
        ApplicationPlatform retrievedAppPlat1 = null, retrievedAppPlat2 = null, retrievedAppPlat3 = null, retrievedAppPlat4 = null;
        for (ApplicationPlatform appPlat : retrievedAppPlats) {
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
        assertEquals(appPlat1.getId(), retrievedAppPlat1.getId());
        assertEquals(appPlat1.getToken(), retrievedAppPlat1.getToken());
        assertNull(retrievedAppPlat1.getExternalServiceToken());
        assertNull(retrievedAppPlat1.getExternalServiceTokenAsBinary());
        assertNull(retrievedAppPlat1.getExternalServicePassword());
        assertNull(retrievedAppPlat1.getExternalServicePasswordAsBinary());
        assertNull(retrievedAppPlat1.getApplication());
        assertNull(retrievedAppPlat1.getPlatform());
        assertEquals(appPlat2.getId(), retrievedAppPlat2.getId());
        assertEquals(appPlat2.getToken(), retrievedAppPlat2.getToken());
        assertNull(retrievedAppPlat2.getExternalServiceToken());
        assertNull(retrievedAppPlat2.getExternalServiceTokenAsBinary());
        assertNull(retrievedAppPlat2.getExternalServicePassword());
        assertNull(retrievedAppPlat2.getExternalServicePasswordAsBinary());
        assertNull(retrievedAppPlat2.getApplication());
        assertNull(retrievedAppPlat2.getPlatform());
        assertEquals(appPlat3.getId(), retrievedAppPlat3.getId());
        assertEquals(appPlat3.getToken(), retrievedAppPlat3.getToken());
        assertNull(retrievedAppPlat3.getExternalServiceToken());
        assertNull(retrievedAppPlat3.getExternalServiceTokenAsBinary());
        assertNull(retrievedAppPlat3.getExternalServicePassword());
        assertNull(retrievedAppPlat3.getExternalServicePasswordAsBinary());
        assertNull(retrievedAppPlat3.getApplication());
        assertNull(retrievedAppPlat3.getPlatform());
        assertEquals(appPlat4.getId(), retrievedAppPlat4.getId());
        assertEquals(appPlat4.getToken(), retrievedAppPlat4.getToken());
        assertNull(retrievedAppPlat4.getExternalServiceToken());
        assertNull(retrievedAppPlat4.getExternalServiceTokenAsBinary());
        assertNull(retrievedAppPlat4.getExternalServicePassword());
        assertNull(retrievedAppPlat4.getExternalServicePasswordAsBinary());
        assertNull(retrievedAppPlat4.getApplication());
        assertNull(retrievedAppPlat4.getPlatform());
    }

    @Test
    public void testSaveNonExistentAppPlatSingle() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat1.setApplication(app1);

        ApplicationPlatform[] appPlats = { appPlat1 };
        List<ApplicationPlatform> savedAppPlats = applicationPlatformDAO.saveApplicationPlatform(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        ApplicationPlatform savedAppPlat1 = savedAppPlats.get(0);
        appPlat1.setId(savedAppPlat1.getId());
        assertEquals(ApplicationPlatform1.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform1.TOKEN, savedAppPlat1.getToken());
        assertThat(savedAppPlat1.getExternalServiceToken(), equalTo(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat1.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(savedAppPlat1.getExternalServicePasswordAsBinary(),
                equalTo(appPlat1.getExternalServicePasswordAsBinary()));
        assertEquals(app1.getId(), savedAppPlat1.getApplication().getId());
        assertEquals(1, savedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
    }

    @Test
    public void testSaveNonExistentAppPlatMultiple() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat4 = ApplicationPlatform4.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app1);
        appPlat4.setApplication(app2);

        ApplicationPlatform[] appPlats = { appPlat1, appPlat2, appPlat3, appPlat4 };
        List<ApplicationPlatform> savedAppPlats = applicationPlatformDAO.saveApplicationPlatform(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        ApplicationPlatform savedAppPlat1 = null, savedAppPlat2 = null, savedAppPlat3 = null, savedAppPlat4 = null;
        for (ApplicationPlatform appPlat : savedAppPlats) {
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
        assertThat(savedAppPlat1.getExternalServiceToken(), equalTo(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat1.getExternalServiceTokenAsBinary(), equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat1.getExternalServicePassword(), equalTo(appPlat1.getExternalServicePassword()));
        assertThat(savedAppPlat1.getExternalServicePasswordAsBinary(),
                equalTo(appPlat1.getExternalServicePasswordAsBinary()));
        assertEquals(app1.getId(), savedAppPlat1.getApplication().getId());
        assertEquals(3, savedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat2.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat2.getToken());
        assertThat(savedAppPlat2.getExternalServiceToken(), equalTo(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat2.getExternalServiceTokenAsBinary(), equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat2.getExternalServicePassword(), equalTo(appPlat2.getExternalServicePassword()));
        assertThat(savedAppPlat2.getExternalServicePasswordAsBinary(),
                equalTo(appPlat2.getExternalServicePasswordAsBinary()));
        assertEquals(app1.getId(), savedAppPlat2.getApplication().getId());
        assertEquals(3, savedAppPlat2.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat2.getId());
        assertTrue(savedAppPlat2.getId() > 0);
        assertEquals(ApplicationPlatform3.PLATFORM, savedAppPlat3.getPlatform());
        assertEquals(ApplicationPlatform3.TOKEN, savedAppPlat3.getToken());
        assertThat(savedAppPlat3.getExternalServiceToken(), equalTo(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat3.getExternalServiceTokenAsBinary(), equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat3.getExternalServicePassword(), equalTo(appPlat3.getExternalServicePassword()));
        assertThat(savedAppPlat3.getExternalServicePasswordAsBinary(),
                equalTo(appPlat3.getExternalServicePasswordAsBinary()));
        assertEquals(app1.getId(), savedAppPlat3.getApplication().getId());
        assertEquals(3, savedAppPlat3.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat3.getId());
        assertTrue(savedAppPlat3.getId() > 0);
        assertEquals(ApplicationPlatform4.PLATFORM, savedAppPlat4.getPlatform());
        assertEquals(ApplicationPlatform4.TOKEN, savedAppPlat4.getToken());
        assertThat(savedAppPlat4.getExternalServiceToken(), equalTo(ApplicationPlatform4.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat4.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform4.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat4.getExternalServicePassword(), equalTo(appPlat4.getExternalServicePassword()));
        assertThat(savedAppPlat4.getExternalServicePasswordAsBinary(),
                equalTo(appPlat4.getExternalServicePasswordAsBinary()));
        assertEquals(app2.getId(), savedAppPlat4.getApplication().getId());
        assertEquals(1, savedAppPlat4.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat4.getId());
        assertTrue(savedAppPlat4.getId() > 0);
    }

    @Test
    public void testSaveExistingAppPlatSingle() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat1.setApplication(app1);
        TestDataHelper.createApplicationPlatform(appPlat1);

        appPlat1.setToken(ApplicationPlatform2.TOKEN);
        appPlat1.setExternalServiceToken(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN);
        appPlat1.setExternalServicePassword(ApplicationPlatform2.EXTERNAL_SERVICE_PASSWORD);
        appPlat1.setPlatform(ApplicationPlatform2.PLATFORM);
        appPlat1.setApplication(app2);

        ApplicationPlatform[] appPlats = { appPlat1 };
        List<ApplicationPlatform> savedAppPlats = applicationPlatformDAO.saveApplicationPlatform(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        ApplicationPlatform savedAppPlat1 = savedAppPlats.get(0);
        appPlat1.setId(savedAppPlat1.getId());
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat1.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat1.getToken());
        assertThat(savedAppPlat1.getExternalServiceToken(), equalTo(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat1.getExternalServicePassword(), equalTo(ApplicationPlatform2.EXTERNAL_SERVICE_PASSWORD));
        assertThat(savedAppPlat1.getExternalServicePasswordAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform2.EXTERNAL_SERVICE_PASSWORD)));
        assertEquals(app2.getId(), savedAppPlat1.getApplication().getId());
        assertEquals(1, savedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
    }

    @Test
    public void testSaveExistingAppPlatMultiple() throws Exception
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
        appPlat1.setExternalServiceToken(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN);
        appPlat1.setPlatform(ApplicationPlatform3.PLATFORM);
        appPlat1.setApplication(app2);

        appPlat2.setToken(ApplicationPlatform1.TOKEN);
        appPlat2.setExternalServiceToken(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN);
        appPlat2.setExternalServicePasswordAsBinary(null);
        appPlat2.setPlatform(ApplicationPlatform1.PLATFORM);
        appPlat2.setApplication(app2);

        appPlat3.setToken(ApplicationPlatform2.TOKEN);
        appPlat3.setExternalServiceToken(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN);
        appPlat3.setExternalServicePassword(ApplicationPlatform2.EXTERNAL_SERVICE_PASSWORD);
        appPlat3.setPlatform(ApplicationPlatform2.PLATFORM);
        appPlat3.setApplication(app1);

        ApplicationPlatform[] appPlats = { appPlat1, appPlat2, appPlat3 };
        List<ApplicationPlatform> savedAppPlats = applicationPlatformDAO.saveApplicationPlatform(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        ApplicationPlatform savedAppPlat1 = null, savedAppPlat2 = null, savedAppPlat3 = null;
        for (ApplicationPlatform appPlat : savedAppPlats) {
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
        assertThat(savedAppPlat1.getExternalServiceToken(), equalTo(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat1.getExternalServicePassword(), equalTo(ApplicationPlatform3.EXTERNAL_SERVICE_PASSWORD));
        assertThat(savedAppPlat1.getExternalServicePasswordAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform3.EXTERNAL_SERVICE_PASSWORD)));
        assertEquals(app2.getId(), savedAppPlat1.getApplication().getId());
        assertEquals(2, savedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
        assertEquals(ApplicationPlatform1.PLATFORM, savedAppPlat2.getPlatform());
        assertEquals(ApplicationPlatform1.TOKEN, savedAppPlat2.getToken());
        assertThat(savedAppPlat2.getExternalServiceToken(), equalTo(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat2.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform1.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat2.getExternalServicePassword(), equalTo(ApplicationPlatform1.EXTERNAL_SERVICE_PASSWORD));
        assertThat(savedAppPlat2.getExternalServicePasswordAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform1.EXTERNAL_SERVICE_PASSWORD)));
        assertEquals(app2.getId(), savedAppPlat2.getApplication().getId());
        assertEquals(2, savedAppPlat2.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat2.getId());
        assertTrue(savedAppPlat2.getId() > 0);
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat3.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat3.getToken());
        assertThat(savedAppPlat3.getExternalServiceToken(), equalTo(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat3.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat3.getExternalServicePassword(), equalTo(ApplicationPlatform2.EXTERNAL_SERVICE_PASSWORD));
        assertThat(savedAppPlat3.getExternalServicePasswordAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform2.EXTERNAL_SERVICE_PASSWORD)));
        assertEquals(app1.getId(), savedAppPlat3.getApplication().getId());
        assertEquals(1, savedAppPlat3.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat3.getId());
        assertTrue(savedAppPlat3.getId() > 0);
    }

    /*
     * Insert one application platform and then perform a save with 3 - one of
     * which should be an update while the other 2 are inserts.
     */
    @Test
    public void testUpsertAppPlat() throws Exception
    {
        appPlat1 = ApplicationPlatform1.getDomainObject();
        appPlat2 = ApplicationPlatform2.getDomainObject();
        appPlat3 = ApplicationPlatform3.getDomainObject();
        appPlat1.setApplication(app1);
        appPlat2.setApplication(app1);
        appPlat3.setApplication(app2);
        TestDataHelper.createApplicationPlatform(appPlat1);

        appPlat1.setToken(ApplicationPlatform4.TOKEN);
        appPlat1.setExternalServiceToken(ApplicationPlatform4.EXTERNAL_SERVICE_TOKEN);
        appPlat1.setExternalServicePassword(ApplicationPlatform4.EXTERNAL_SERVICE_PASSWORD);
        appPlat1.setPlatform(ApplicationPlatform4.PLATFORM);
        appPlat1.setApplication(app2);

        ApplicationPlatform[] appPlats = { appPlat1, appPlat2, appPlat3 };
        List<ApplicationPlatform> savedAppPlats = applicationPlatformDAO.saveApplicationPlatform(appPlats);
        assertEquals(appPlats.length, savedAppPlats.size());
        ApplicationPlatform savedAppPlat1 = null, savedAppPlat2 = null, savedAppPlat3 = null;
        for (ApplicationPlatform appPlat : savedAppPlats) {
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
        assertThat(savedAppPlat1.getExternalServiceToken(), equalTo(ApplicationPlatform4.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat1.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform4.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat1.getExternalServicePassword(), equalTo(ApplicationPlatform4.EXTERNAL_SERVICE_PASSWORD));
        assertThat(savedAppPlat1.getExternalServicePasswordAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform4.EXTERNAL_SERVICE_PASSWORD)));
        assertEquals(app2.getId(), savedAppPlat1.getApplication().getId());
        assertEquals(2, savedAppPlat1.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat1.getId());
        assertTrue(savedAppPlat1.getId() > 0);
        assertEquals(ApplicationPlatform2.PLATFORM, savedAppPlat2.getPlatform());
        assertEquals(ApplicationPlatform2.TOKEN, savedAppPlat2.getToken());
        assertThat(savedAppPlat2.getExternalServiceToken(), equalTo(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat2.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform2.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat2.getExternalServicePassword(), equalTo(appPlat2.getExternalServicePassword()));
        assertThat(savedAppPlat2.getExternalServicePasswordAsBinary(),
                equalTo(appPlat2.getExternalServicePasswordAsBinary()));
        assertEquals(app1.getId(), savedAppPlat2.getApplication().getId());
        assertEquals(1, savedAppPlat2.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat2.getId());
        assertTrue(savedAppPlat2.getId() > 0);
        assertEquals(ApplicationPlatform3.PLATFORM, savedAppPlat3.getPlatform());
        assertEquals(ApplicationPlatform3.TOKEN, savedAppPlat3.getToken());
        assertThat(savedAppPlat3.getExternalServiceToken(), equalTo(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN));
        assertThat(savedAppPlat3.getExternalServiceTokenAsBinary(),
                equalTo(EncodeHelper.encodeBase64Image(ApplicationPlatform3.EXTERNAL_SERVICE_TOKEN)));
        assertThat(savedAppPlat3.getExternalServicePassword(), equalTo(appPlat3.getExternalServicePassword()));
        assertThat(savedAppPlat3.getExternalServicePasswordAsBinary(),
                equalTo(appPlat3.getExternalServicePasswordAsBinary()));
        assertEquals(app2.getId(), savedAppPlat3.getApplication().getId());
        assertEquals(2, savedAppPlat3.getApplication().getApplicationPlatforms().size());
        assertNotNull(savedAppPlat3.getId());
        assertTrue(savedAppPlat3.getId() > 0);
    }
}
