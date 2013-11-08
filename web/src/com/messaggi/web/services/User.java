package com.messaggi.web.services;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public interface User
{
    class Messages
    {
        protected static final String USER_NOT_SET_IN_REQUEST = "Request did not contain an instance of User.";

        protected static final String MISSING_PATH_PARAMETER = "Request did not contain required path parameter.";

        protected static final String PATH_PARAMETER_REQUEST_BODY_MISMATCH = "Path parameter does not match request body.";
    }

    static final String NEW_USER_URI_FRAGMENT_FORMAT = "/id/{0}";

    @XmlRootElement
    class UserRequest
    {
        @XmlElement(name = "user")
        private com.messaggi.persistence.domain.User user;

        public com.messaggi.persistence.domain.User getUser()
        {
            return user;
        }

        public void setUser(com.messaggi.persistence.domain.User user)
        {
            this.user = user;
        }

        public UserRequest()
        {

        }

        public UserRequest(com.messaggi.persistence.domain.User user)
        {
            this.user = user;
        }
    }
    
    @XmlRootElement
    class UserResponse
    {
        @XmlElement(name = "user")
        private com.messaggi.persistence.domain.User user;

        public com.messaggi.persistence.domain.User getUser()
        {
            return user;
        }

        public void setUser(com.messaggi.persistence.domain.User user)
        {
            this.user = user;
        }

        public UserResponse()
        {

        }

        public UserResponse(com.messaggi.persistence.domain.User user)
        {
            this.user = user;
        }
    }

    Response registerNewUser(UriInfo uriInfo, UserRequest request) throws WebServiceException;

    Response getUserByEmail(String email) throws WebServiceException;

    Response getUserById(Long id) throws WebServiceException;

    Response updateUser(Long id, UserRequest request) throws WebServiceException;

    Response inactivateUserById(Long id) throws WebServiceException;
}

