package com.messaggi.web.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.DAOFactory;
import com.messaggi.persistence.dao.DAOFactory.Factory;
import com.messaggi.persistence.dao.UserDAO;

@Path("/user")
public class UserImpl implements User
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public UserResponse RegisterNewUser(UserRequest request)
    {
        UserResponse response = new UserResponse();
        if (request.getUser() != null) {
            com.messaggi.persistence.domain.User newUser = new com.messaggi.persistence.domain.User();
            newUser.setName(request.getUser().getName());
            newUser.setEmail(request.getUser().getEmail());
            newUser.setPhone(request.getUser().getPhone());
            newUser.setPassword(request.getUser().getPassword());
            newUser.setLocale(request.getUser().getLocale());
            List<com.messaggi.persistence.domain.User> newUsers = new ArrayList<>();
            newUsers.add(newUser);

            DAOFactory daoFactory = DAOFactory.getDAOFactory(Factory.PostgreSQL);
            UserDAO userDAO = daoFactory.getUserDAO();
            try {
                List<com.messaggi.persistence.domain.User> users = userDAO.insertUser(newUsers);
                if (users.size() > 0) {
                    response.setUser(users.get(0));
                }
            } catch (DAOException e) {

            }
        }
        return response;
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

