package com.messaggi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.junit.WebServiceTestCase;
import com.messaggi.service.TokenGenerator.GenerateTokenResponse;

public class TestTokenGenerator extends WebServiceTestCase
{
    private WebTarget webTarget;

    private static final int TOKEN_LENGTH = 36;

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
        webTarget = client.target("http://localhost:8080/messaggi/service");
    }

    @Test
    public void testGenerateTokenJSON()
    {
        WebTarget utilWebTarget = webTarget.path("util");
        WebTarget tokenWebTarget = utilWebTarget.path("token");
        Invocation.Builder invocationBuilder = tokenWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.get();
        assertEquals(Response.Status.OK.getFamily(), response.getStatusInfo().getFamily());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        GenerateTokenResponse gtr = response.readEntity(GenerateTokenResponse.class);
        assertNotNull(gtr);
        assertEquals(TOKEN_LENGTH, gtr.getToken().length());
    }

    @Test
    public void testGenerateTokenXML()
    {
        WebTarget utilWebTarget = webTarget.path("util");
        WebTarget tokenWebTarget = utilWebTarget.path("token");
        Invocation.Builder invocationBuilder = tokenWebTarget.request(MediaType.APPLICATION_XML_TYPE);
        Response response = invocationBuilder.get();
        assertEquals(Response.Status.OK.getFamily(), response.getStatusInfo().getFamily());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        GenerateTokenResponse gtr = response.readEntity(GenerateTokenResponse.class);
        assertNotNull(gtr);
        assertEquals(TOKEN_LENGTH, gtr.getToken().length());
    }
}

