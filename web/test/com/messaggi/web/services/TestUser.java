package com.messaggi.web.services;

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
import org.junit.Before;
import org.junit.Test;

import com.messaggi.junit.WebServiceTestCase;
import com.messaggi.persistence.domain.User;
import com.messaggi.services.TokenGenerator.GenerateTokenResponse;
import com.messaggi.web.services.User.UserRequest;

public class TestUser extends WebServiceTestCase
{
    private WebTarget webTarget;

    private static final String NAME1 = "Justin C. Wolfe";

    private static final String EMAIL1 = "jcw_222@yahoo.com";

    private static final String PHONE1 = "617-549-2403";

    private static final String PASSWD1 = "mypassword";

    private static final Locale LOCALE1 = Locale.US;

    private static final long PHONE_PARSED1 = 6175492403L;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        webTarget = client.target("http://localhost:8080/messaggi/services/user");
    }

    @Test
    public void testRegisterNewUser()
    {
        User user = new User();
        user.setName(NAME1);
        user.setEmail(EMAIL1);
        user.setPhone(PHONE1);
        user.setPassword(PASSWD1);
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
}

