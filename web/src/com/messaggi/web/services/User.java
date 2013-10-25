package com.messaggi.web.services;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public interface User
{
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

    UserResponse RegisterNewUser(String name, String email, String phone, String password, String locale);

    UserResponse UpdateUserName(String newName);

    UserResponse UpdateUserEmail(String newEmail);

    UserResponse UpdateUserPhone(String newPhone);

    UserResponse UpdateUserPassword(String newPassword);

    UserResponse UpdateUserLocale(String newLocale);

    UserResponse InactivateUser();

    UserResponse GetUserById(String id);

    UserResponse GetUserByEmail(String email);
}

