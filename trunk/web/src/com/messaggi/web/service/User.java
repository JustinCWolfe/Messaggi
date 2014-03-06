package com.messaggi.web.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

public interface User
{
    class Messages
    {
        protected static final String USER_NOT_SET_IN_REQUEST = "Request did not contain an instance of User.";

        protected static final String MISSING_PATH_PARAMETER = "Request did not contain required path parameter.";

        protected static final String PATH_PARAMETER_REQUEST_BODY_MISMATCH = "Path parameter does not match request body.";
    }

    static final String NEW_USER_URI_FRAGMENT_FORMAT = "/id/%s";

    @XmlRootElement
    class UserRequest
    {
        private com.messaggi.domain.User user;

        public com.messaggi.domain.User getUser()
        {
            return user;
        }

        public void setUser(com.messaggi.domain.User user)
        {
            this.user = user;
        }

        public UserRequest()
        {

        }

        public UserRequest(com.messaggi.domain.User user)
        {
            this.user = user;
        }
    }
    
    @XmlRootElement
    class UserResponse
    {
        private com.messaggi.domain.User user;

        public com.messaggi.domain.User getUser()
        {
            return user;
        }

        public void setUser(com.messaggi.domain.User user)
        {
            this.user = user;
        }

        public UserResponse()
        {

        }

        public UserResponse(com.messaggi.domain.User user)
        {
            this.user = user;
        }
    }

    Response registerNewUser(UriInfo uriInfo, UserRequest request) throws Exception;

    Response getUserByEmail(String email) throws Exception;

    Response getUserById(Integer id) throws Exception;

    Response updateUser(Integer id, UserRequest request) throws Exception;

    Response inactivateUserById(Integer id) throws Exception;
}

