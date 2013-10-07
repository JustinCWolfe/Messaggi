package com.messaggi.dao;

// Generated Oct 7, 2013 3:43:26 PM by Hibernate Tools 4.0.0

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Device.
 * @see com.messaggi.dao.Device
 * @author Hibernate Tools
 */
@Stateless
public class DeviceHome
{

    private static final Log log = LogFactory.getLog(DeviceHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Device transientInstance)
    {
        log.debug("persisting Device instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Device persistentInstance)
    {
        log.debug("removing Device instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Device merge(Device detachedInstance)
    {
        log.debug("merging Device instance");
        try {
            Device result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Device findById(long id)
    {
        log.debug("getting Device instance with id: " + id);
        try {
            Device instance = entityManager.find(Device.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
