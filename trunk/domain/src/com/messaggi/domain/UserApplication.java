package com.messaggi.domain;

// Generated Oct 11, 2013 3:07:40 PM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * UserApplication generated by hbm2java
 */
@Entity
@Table(name = "user_application", schema = "public")
public class UserApplication implements java.io.Serializable
{

    private long applicationId;

    private User user;

    private Application application;

    public UserApplication()
    {
    }

    public UserApplication(User user, Application application)
    {
        this.user = user;
        this.application = application;
    }

    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "application"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "application_id", unique = true, nullable = false)
    public long getApplicationId()
    {
        return this.applicationId;
    }

    public void setApplicationId(long applicationId)
    {
        this.applicationId = applicationId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public Application getApplication()
    {
        return this.application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

}
