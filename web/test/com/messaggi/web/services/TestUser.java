package com.messaggi.web.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;
import org.junit.Test;

import com.messaggi.junit.MessaggiTestCase;
import com.messaggi.services.TokenGenerator.GenerateTokenResponse;

public class TestUser extends MessaggiTestCase
{
    private WebTarget webTarget;

    @Override
    @Before
    public void setUp() throws Exception
    {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        webTarget = client.target("http://localhost:8080/messaggi/services");
    }

    @Test
    public void test()
    {
        WebTarget utilWebTarget = webTarget.path("util");
        WebTarget tokenWebTarget = utilWebTarget.path("token");
        Invocation.Builder invocationBuilder = tokenWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.get();
        System.out.println(response.getStatus());
        assertEquals(Response.Status.OK.getFamily(), response.getStatusInfo().getFamily());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        GenerateTokenResponse gtr = response.readEntity(GenerateTokenResponse.class);
        assertNotNull(gtr);
    }
}

