package com.messaggi.services;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/util")
public class TokenGeneratorImpl implements TokenGenerator
{
    private String getToken()
    {
        return UUID.randomUUID().toString();
    }

    @GET
    @Path("/token")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Override
    public GenerateTokenResponse generateToken()
    {
        return new GenerateTokenResponse(getToken());
    }
}

