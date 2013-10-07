package com.messaggi.dao;

// Generated Oct 7, 2013 3:43:26 PM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class DeviceAttribute.
 * @see com.messaggi.dao.DeviceAttribute
 * @author Hibernate Tools
 */
@Stateless
public class DeviceAttributeHome
{

    private static final Log log = LogFactory.getLog(DeviceAttributeHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(DeviceAttribute transientInstance)
    {
        log.debug("persisting DeviceAttribute instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(DeviceAttribute persistentInstance)
    {
        log.debug("removing DeviceAttribute instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public DeviceAttribute merge(DeviceAttribute detachedInstance)
    {
        log.debug("merging DeviceAttribute instance");
        try {
            DeviceAttribute result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public DeviceAttribute findById(DeviceAttributeId id)
    {
        log.debug("getting DeviceAttribute instance with id: " + id);
        try {
            DeviceAttribute instance = entityManager.find(DeviceAttribute.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
