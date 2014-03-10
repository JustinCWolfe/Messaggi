package com.messaggi.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.domain.User;
import com.messaggi.junit.WebServiceTestCase;
import com.messaggi.service.TokenGenerator.GenerateTokenResponse;
import com.messaggi.web.service.User.UserRequest;

public class TestUser extends WebServiceTestCase
{
    private WebTarget webTarget;

    private static final String NAME1 = "Test User 1";

    private static final String NAME2 = "Test User 2";

    private static final String EMAIL1 = "test_user1@yahoo.com";

    private static final String EMAIL2 = "test_user2@yahoo.com";

    private static final String PHONE1 = "617-549-2403";

    private static final String PHONE2 = "617-549-8277";

    private static final String PASSWD_HASH1 = "test_user_1_pwd";

    private static final String PASSWD_SALT1 = "some_salt1";

    private static final String PASSWD_HASH2 = "test_user_2_pwd";

    private static final String PASSWD_SALT2 = "some_salt2";

    private static final Locale LOCALE1 = Locale.US;

    private static final Locale LOCALE2 = Locale.ITALY;

    private static final long PHONE1_PARSED = 6175492403L;

    private static final long PHONE2_PARSED = 6175498277L;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        webServiceSuiteSetUp();
    }

    @AfterClass
    public static void tearDownAfterClassClass() throws Exception
    {
        webServiceSuiteTearDown();
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        webTarget = client.target("http://localhost:8080/messaggi/service/user");
    }

    @Test
    public void testRegisterNewUser()
    {
        User user = new User();
        user.setName(NAME1);
        user.setEmail(EMAIL1);
        user.setPhone(PHONE1);
        user.setPasswordHash(PASSWD_HASH1);
        user.setPasswordSalt(PASSWD_SALT1);
        user.setLocale(LOCALE1);
        UserRequest request = new UserRequest(user);

        Invocation.Builder invocationBuilder = webTarget.request();
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        System.out.println(response.getStatus());
        assertEquals(Response.Status.OK.getFamily(), response.getStatusInfo().getFamily());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        GenerateTokenResponse gtr = response.readEntity(GenerateTokenResponse.class);
        assertNotNull(gtr);
    }

    @Test
    public void testGetUserByEmail()
    {

    }

    @Test
    public void testGetUserById()
    {

    }

    @Test
    public void testUpdateUser()
    {

    }

    @Test
    public void testInactivateUserById()
    {
    }
}

