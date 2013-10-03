package com.messaggi.dao;

// Generated Oct 2, 2013 11:09:04 AM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messaggi.domain.ApplicationPlatformAttribute;
import com.messaggi.domain.ApplicationPlatformAttributeId;

/**
 * Home object for domain model class ApplicationPlatformAttribute.
 * @see com.messaggi.dao.ApplicationPlatformAttribute
 * @author Hibernate Tools
 */
@Stateless
public class ApplicationPlatformAttributeHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformAttributeHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(ApplicationPlatformAttribute transientInstance)
    {
        log.debug("persisting ApplicationPlatformAttribute instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(ApplicationPlatformAttribute persistentInstance)
    {
        log.debug("removing ApplicationPlatformAttribute instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public ApplicationPlatformAttribute merge(ApplicationPlatformAttribute detachedInstance)
    {
        log.debug("merging ApplicationPlatformAttribute instance");
        try {
            ApplicationPlatformAttribute result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ApplicationPlatformAttribute findById(ApplicationPlatformAttributeId id)
    {
        log.debug("getting ApplicationPlatformAttribute instance with id: " + id);
        try {
            ApplicationPlatformAttribute instance = entityManager.find(ApplicationPlatformAttribute.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
