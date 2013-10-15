package com.messaggi.persistence.dao;

// Generated Oct 15, 2013 1:21:19 PM by Hibernate Tools 4.0.0

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.messaggi.persistence.domain.DeviceKey;

/**
 * Home object for domain model class DeviceKey.
 * @see com.messaggi.persistence.dao.DeviceKey
 * @author Hibernate Tools
 */
public class DeviceKeyHome
{

    private static final Log log = LogFactory.getLog(DeviceKeyHome.class);

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

    public void persist(DeviceKey transientInstance)
    {
        log.debug("persisting DeviceKey instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(DeviceKey instance)
    {
        log.debug("attaching dirty DeviceKey instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(DeviceKey instance)
    {
        log.debug("attaching clean DeviceKey instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(DeviceKey persistentInstance)
    {
        log.debug("deleting DeviceKey instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public DeviceKey merge(DeviceKey detachedInstance)
    {
        log.debug("merging DeviceKey instance");
        try {
            DeviceKey result = (DeviceKey) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public DeviceKey findById(java.lang.String id)
    {
        log.debug("getting DeviceKey instance with id: " + id);
        try {
            DeviceKey instance = (DeviceKey) sessionFactory.getCurrentSession().get(
                    "com.messaggi.persistence.dao.DeviceKey", id);
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

    public List<DeviceKey> findByExample(DeviceKey instance)
    {
        log.debug("finding DeviceKey instance by example");
        try {
            List<DeviceKey> results = sessionFactory.getCurrentSession()
                    .createCriteria("com.messaggi.persistence.dao.DeviceKey").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
