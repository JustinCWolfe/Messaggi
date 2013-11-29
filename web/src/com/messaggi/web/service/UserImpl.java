package com.messaggi.web.service;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.WebServiceException;

import com.messaggi.web.dao.UserDAO;

@Path("/user")
public class UserImpl implements User
{
    private final UserDAO userDAO = new UserDAO();

    private static List<com.messaggi.web.domain.User> getDAOParameters(com.messaggi.web.domain.User user)
    {
        List<com.messaggi.web.domain.User> parameters = new ArrayList<>();
        parameters.add(user);
        return parameters;
    }

    private <T> void validateRequest(T requestParameter) throws WebServiceException
    {
        if (requestParameter == null) {
            throw new WebServiceException(Messages.MISSING_PATH_PARAMETER);
        }
    }

    private void validateRequest(UserRequest request) throws WebServiceException
    {
        if (request.getUser() == null) {
            throw new WebServiceException(Messages.USER_NOT_SET_IN_REQUEST);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Override
    public Response registerNewUser(@Context UriInfo uriInfo, UserRequest request) throws NamingException,
        SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
        WebServiceException
    {
        validateRequest(request);
        UserResponse response = new UserResponse();
        String uriFragment = null;
        List<com.messaggi.web.domain.User> users = userDAO.insertUser(getDAOParameters(request.getUser()));
        if (users.size() > 0) {
            com.messaggi.web.domain.User user = users.get(0);
            response.setUser(user);
            uriFragment = String.format(NEW_USER_URI_FRAGMENT_FORMAT, user.getId().toString());
        }
        URI newResourceURI = uriInfo.getAbsolutePath().resolve(uriFragment);
        return Response.status(Response.Status.CREATED).contentLocation(newResourceURI).entity(response).build();
    }

    @GET
    @Path("/email/{email}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Override
    public Response getUserByEmail(@PathParam("email") String email) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
        WebServiceException
    {
        validateRequest(email);
        UserResponse response = new UserResponse();
        com.messaggi.web.domain.User prototype = new com.messaggi.web.domain.User();
        prototype.setEmail(email);
        List<com.messaggi.web.domain.User> users = userDAO.selectUser(getDAOParameters(prototype));
        if (users.size() > 0) {
            response.setUser(users.get(0));
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Override
    public Response getUserById(@PathParam("id") Long id) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
        WebServiceException
    {
        validateRequest(id);
        UserResponse response = new UserResponse();
        com.messaggi.web.domain.User prototype = new com.messaggi.web.domain.User();
        prototype.setId(id);
        List<com.messaggi.web.domain.User> users = userDAO.selectUser(getDAOParameters(prototype));
        if (users.size() > 0) {
            response.setUser(users.get(0));
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Override
    public Response updateUser(@PathParam("id") Long id, UserRequest request) throws NamingException, SQLException,
        InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
        WebServiceException
    {
        validateRequest(request);
        // Verify that the user id in the uri and the request body are the same.
        if (!id.equals(request.getUser().getId().toString())) {
            throw new WebServiceException(Messages.PATH_PARAMETER_REQUEST_BODY_MISMATCH);
        }
        userDAO.updateUser(getDAOParameters(request.getUser()));
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/id/{id}")
    @Override
    public Response inactivateUserById(Long id) throws NamingException, SQLException, InvocationTargetException,
        NoSuchMethodException, InstantiationException, IllegalAccessException, WebServiceException
    {
        validateRequest(id);
        com.messaggi.web.domain.User prototype = new com.messaggi.web.domain.User();
        prototype.setId(id);
        userDAO.deleteUser(getDAOParameters(prototype));
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
