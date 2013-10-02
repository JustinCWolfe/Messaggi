package com.messaggi.dao;

// Generated Oct 2, 2013 11:09:04 AM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Platform.
 * @see com.messaggi.dao.Platform
 * @author Hibernate Tools
 */
@Stateless
public class PlatformHome
{

    private static final Log log = LogFactory.getLog(PlatformHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Platform transientInstance)
    {
        log.debug("persisting Platform instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Platform persistentInstance)
    {
        log.debug("removing Platform instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Platform merge(Platform detachedInstance)
    {
        log.debug("merging Platform instance");
        try {
            Platform result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Platform findById(long id)
    {
        log.debug("getting Platform instance with id: " + id);
        try {
            Platform instance = entityManager.find(Platform.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
