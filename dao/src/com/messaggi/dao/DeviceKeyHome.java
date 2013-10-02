package com.messaggi.dao;

// Generated Oct 2, 2013 11:09:04 AM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class DeviceKey.
 * @see com.messaggi.dao.DeviceKey
 * @author Hibernate Tools
 */
@Stateless
public class DeviceKeyHome
{

    private static final Log log = LogFactory.getLog(DeviceKeyHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(DeviceKey transientInstance)
    {
        log.debug("persisting DeviceKey instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(DeviceKey persistentInstance)
    {
        log.debug("removing DeviceKey instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public DeviceKey merge(DeviceKey detachedInstance)
    {
        log.debug("merging DeviceKey instance");
        try {
            DeviceKey result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public DeviceKey findById(String id)
    {
        log.debug("getting DeviceKey instance with id: " + id);
        try {
            DeviceKey instance = entityManager.find(DeviceKey.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
