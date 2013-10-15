package com.messaggi.persistence.dao;

// Generated Oct 15, 2013 1:21:19 PM by Hibernate Tools 4.0.0

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.messaggi.persistence.domain.Device;

/**
 * Home object for domain model class Device.
 * @see com.messaggi.persistence.dao.Device
 * @author Hibernate Tools
 */
public class DeviceHome
{

    private static final Log log = LogFactory.getLog(DeviceHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory()
    {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(Device transientInstance)
    {
        log.debug("persisting Device instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(Device instance)
    {
        log.debug("attaching dirty Device instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Device instance)
    {
        log.debug("attaching clean Device instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Device persistentInstance)
    {
        log.debug("deleting Device instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Device merge(Device detachedInstance)
    {
        log.debug("merging Device instance");
        try {
            Device result = (Device) sessionFactory.getCurrentSession().merge(detachedInstance);
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
            Device instance = (Device) sessionFactory.getCurrentSession()
                    .get("com.messaggi.persistence.dao.Device", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<Device> findByExample(Device instance)
    {
        log.debug("finding Device instance by example");
        try {
            List<Device> results = sessionFactory.getCurrentSession()
                    .createCriteria("com.messaggi.persistence.dao.Device").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
