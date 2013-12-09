package com.messaggi.domain;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationPlatform
{
    public enum Platform {
        IOS("IOS"), ANDROID("ANDROID"), WINDOWS("WINDOWS");
        
        private final String value;
        
        public String getValue()
        {
            return value;
        }

        private Platform (String value) {
            this.value = value;
        }
    }

    private Long id;

    private Application application;

    private Platform platform;

    private UUID token;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public Platform getPlatform()
    {
        return platform;
    }

    public void setPlatform(Platform platform)
    {
        this.platform = platform;
    }

    public UUID getToken()
    {
        return token;
    }

    public void setToken(UUID token)
    {
        this.token = token;
    }

    public ApplicationPlatform()
    {

    }
}
