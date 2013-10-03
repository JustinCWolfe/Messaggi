package com.messaggi.dao;

// Generated Oct 2, 2013 11:09:04 AM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messaggi.domain.UserApplication;

/**
 * Home object for domain model class UserApplication.
 * @see com.messaggi.dao.UserApplication
 * @author Hibernate Tools
 */
@Stateless
public class UserApplicationHome
{

    private static final Log log = LogFactory.getLog(UserApplicationHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(UserApplication transientInstance)
    {
        log.debug("persisting UserApplication instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(UserApplication persistentInstance)
    {
        log.debug("removing UserApplication instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public UserApplication merge(UserApplication detachedInstance)
    {
        log.debug("merging UserApplication instance");
        try {
            UserApplication result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public UserApplication findById(long id)
    {
        log.debug("getting UserApplication instance with id: " + id);
        try {
            UserApplication instance = entityManager.find(UserApplication.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
