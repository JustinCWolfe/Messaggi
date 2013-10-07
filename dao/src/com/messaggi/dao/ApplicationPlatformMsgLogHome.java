package com.messaggi.dao;

// Generated Oct 7, 2013 3:43:26 PM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ApplicationPlatformMsgLog.
 * @see com.messaggi.dao.ApplicationPlatformMsgLog
 * @author Hibernate Tools
 */
@Stateless
public class ApplicationPlatformMsgLogHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformMsgLogHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(ApplicationPlatformMsgLog transientInstance)
    {
        log.debug("persisting ApplicationPlatformMsgLog instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(ApplicationPlatformMsgLog persistentInstance)
    {
        log.debug("removing ApplicationPlatformMsgLog instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public ApplicationPlatformMsgLog merge(ApplicationPlatformMsgLog detachedInstance)
    {
        log.debug("merging ApplicationPlatformMsgLog instance");
        try {
            ApplicationPlatformMsgLog result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ApplicationPlatformMsgLog findById(long id)
    {
        log.debug("getting ApplicationPlatformMsgLog instance with id: " + id);
        try {
            ApplicationPlatformMsgLog instance = entityManager.find(ApplicationPlatformMsgLog.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
