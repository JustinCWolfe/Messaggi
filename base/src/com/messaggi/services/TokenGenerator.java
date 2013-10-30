package com.messaggi.services;

import javax.xml.bind.annotation.XmlRootElement;

public interface TokenGenerator
{
    @XmlRootElement
    class GenerateTokenResponse
    {
        private String token;

        public String getToken()
        {
            return token;
        }

        public void setToken(String token)
        {
            this.token = token;
        }

        public GenerateTokenResponse()
        {

        }

        public GenerateTokenResponse(String token)
        {
            this.token = token;
        }
    }

    GenerateTokenResponse generateToken();
}

