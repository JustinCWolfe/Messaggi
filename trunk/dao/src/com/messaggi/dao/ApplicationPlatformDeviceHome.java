package com.messaggi.dao;

// Generated Oct 7, 2013 3:43:26 PM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ApplicationPlatformDevice.
 * @see com.messaggi.dao.ApplicationPlatformDevice
 * @author Hibernate Tools
 */
@Stateless
public class ApplicationPlatformDeviceHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformDeviceHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(ApplicationPlatformDevice transientInstance)
    {
        log.debug("persisting ApplicationPlatformDevice instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(ApplicationPlatformDevice persistentInstance)
    {
        log.debug("removing ApplicationPlatformDevice instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public ApplicationPlatformDevice merge(ApplicationPlatformDevice detachedInstance)
    {
        log.debug("merging ApplicationPlatformDevice instance");
        try {
            ApplicationPlatformDevice result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ApplicationPlatformDevice findById(ApplicationPlatformDeviceId id)
    {
        log.debug("getting ApplicationPlatformDevice instance with id: " + id);
        try {
            ApplicationPlatformDevice instance = entityManager.find(ApplicationPlatformDevice.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
