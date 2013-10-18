package com.messaggi.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.messaggi.services.TokenGenerator.GenerateTokenResponse;
import com.messaggi.util.JsonMoxyConfigurationContextResolver;

public class TestTokenGenerator
{
    private WebTarget webTarget;

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
        ClientConfig clientConfig = new ClientConfig();
        //clientConfig.register(MyClientResponseFilter.class);
        //clientConfig.register(new AnotherClientFilter());
        Client client = ClientBuilder.newClient(clientConfig);
        webTarget = client.target("http://localhost:8080/messaggi/services");
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testGenerateToken()
    {
        // The line bellow that registers MOXy feature can be
        // omitted if FEATURE_AUTO_DISCOVERY_DISABLE is
        // not disabled.
        final Client client = ClientBuilder.newBuilder().register(MoxyJsonFeature.class)
                .register(JsonMoxyConfigurationContextResolver.class).build();
        WebTarget webTarget = client.target("http://localhost:8080/messaggi/services/util/token");
        GenerateTokenResponse response = webTarget.request(MediaType.APPLICATION_XML_TYPE).get(
                GenerateTokenResponse.class);
    }

    @Test
    public void testGenerateToken2() throws Exception
    {
        WebTarget utilWebTarget = webTarget.path("util");
        WebTarget token2WebTarget = utilWebTarget.path("token2");
        Invocation.Builder invocationBuilder = token2WebTarget.request(MediaType.TEXT_PLAIN_TYPE);
        Response response = invocationBuilder.get();
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }
}

