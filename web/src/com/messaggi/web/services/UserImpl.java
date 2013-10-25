package com.messaggi.web.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserImpl implements User
{
    @Override
    public UserResponse RegisterNewUser(String name, String email, String phone, String password, String locale)
    {
        return new UserResponse();
    }

    @Override
    public UserResponse UpdateUserName(String newName)
    {

        return new UserResponse();
    }

    @Override
    public UserResponse UpdateUserEmail(String newEmail)
    {

        return new UserResponse();
    }

    @Override
    public UserResponse UpdateUserPhone(String newPhone)
    {

        return new UserResponse();
    }

    @Override
    public UserResponse UpdateUserPassword(String newPassword)
    {

        return new UserResponse();
    }

    @Override
    public UserResponse UpdateUserLocale(String newLocale)
    {

        return new UserResponse();
    }

    @Override
    public UserResponse InactivateUser()
    {

        return new UserResponse();
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public UserResponse GetUserById(@PathParam("id") String id)
    {
        return new UserResponse(new com.messaggi.persistence.domain.User());
    }

    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public UserResponse GetUserByEmail(@PathParam("email") String email)
    {
        return new UserResponse(new com.messaggi.persistence.domain.User());
    }
}

