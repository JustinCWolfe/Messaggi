package com.messaggi.dao;

// Generated Oct 7, 2013 3:43:26 PM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messaggi.domain.ApplicationPlatform;

/**
 * Home object for domain model class ApplicationPlatform.
 * @see com.messaggi.dao.ApplicationPlatform
 * @author Hibernate Tools
 */
@Stateless
public class ApplicationPlatformHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(ApplicationPlatform transientInstance)
    {
        log.debug("persisting ApplicationPlatform instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(ApplicationPlatform persistentInstance)
    {
        log.debug("removing ApplicationPlatform instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public ApplicationPlatform merge(ApplicationPlatform detachedInstance)
    {
        log.debug("merging ApplicationPlatform instance");
        try {
            ApplicationPlatform result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ApplicationPlatform findById(long id)
    {
        log.debug("getting ApplicationPlatform instance with id: " + id);
        try {
            ApplicationPlatform instance = entityManager.find(ApplicationPlatform.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
