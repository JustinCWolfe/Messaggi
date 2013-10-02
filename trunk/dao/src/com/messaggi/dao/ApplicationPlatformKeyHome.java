package com.messaggi.dao;

// Generated Oct 2, 2013 11:09:04 AM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ApplicationPlatformKey.
 * @see com.messaggi.dao.ApplicationPlatformKey
 * @author Hibernate Tools
 */
@Stateless
public class ApplicationPlatformKeyHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformKeyHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(ApplicationPlatformKey transientInstance)
    {
        log.debug("persisting ApplicationPlatformKey instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(ApplicationPlatformKey persistentInstance)
    {
        log.debug("removing ApplicationPlatformKey instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public ApplicationPlatformKey merge(ApplicationPlatformKey detachedInstance)
    {
        log.debug("merging ApplicationPlatformKey instance");
        try {
            ApplicationPlatformKey result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ApplicationPlatformKey findById(String id)
    {
        log.debug("getting ApplicationPlatformKey instance with id: " + id);
        try {
            ApplicationPlatformKey instance = entityManager.find(ApplicationPlatformKey.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
