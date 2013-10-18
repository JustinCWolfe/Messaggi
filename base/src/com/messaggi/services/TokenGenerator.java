package com.messaggi.services;

import javax.xml.bind.annotation.XmlRootElement;

public interface TokenGenerator
{
    @XmlRootElement
    public class GenerateTokenResponse
    {
        private final String token;

        public String getToken()
        {
            return token;
        }

        public GenerateTokenResponse(String token)
        {
            this.token = token;
        }
    }

    public GenerateTokenResponse generateToken();
}

