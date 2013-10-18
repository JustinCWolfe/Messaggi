package com.messaggi.services;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/util")
public class TokenGeneratorImpl implements TokenGenerator
{
    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public GenerateTokenResponse generateToken()
    {
        return new GenerateTokenResponse(UUID.randomUUID().toString());
    }

    @GET
    @Path("/token2")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken2()
    {
        return UUID.randomUUID().toString();
    }
}

