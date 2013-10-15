package com.messaggi.persistence.dao;

// Generated Oct 15, 2013 1:21:19 PM by Hibernate Tools 4.0.0

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.messaggi.persistence.domain.ApplicationPlatformDevice;
import com.messaggi.persistence.domain.ApplicationPlatformDeviceId;

/**
 * Home object for domain model class ApplicationPlatformDevice.
 * @see com.messaggi.persistence.dao.ApplicationPlatformDevice
 * @author Hibernate Tools
 */
public class ApplicationPlatformDeviceHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformDeviceHome.class);

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

    public void persist(ApplicationPlatformDevice transientInstance)
    {
        log.debug("persisting ApplicationPlatformDevice instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(ApplicationPlatformDevice instance)
    {
        log.debug("attaching dirty ApplicationPlatformDevice instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(ApplicationPlatformDevice instance)
    {
        log.debug("attaching clean ApplicationPlatformDevice instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(ApplicationPlatformDevice persistentInstance)
    {
        log.debug("deleting ApplicationPlatformDevice instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ApplicationPlatformDevice merge(ApplicationPlatformDevice detachedInstance)
    {
        log.debug("merging ApplicationPlatformDevice instance");
        try {
            ApplicationPlatformDevice result = (ApplicationPlatformDevice) sessionFactory.getCurrentSession().merge(
                    detachedInstance);
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
            ApplicationPlatformDevice instance = (ApplicationPlatformDevice) sessionFactory.getCurrentSession().get(
                    "com.messaggi.persistence.dao.ApplicationPlatformDevice", id);
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

    public List<ApplicationPlatformDevice> findByExample(ApplicationPlatformDevice instance)
    {
        log.debug("finding ApplicationPlatformDevice instance by example");
        try {
            List<ApplicationPlatformDevice> results = sessionFactory
                    .getCurrentSession().createCriteria("com.messaggi.persistence.dao.ApplicationPlatformDevice")
                    .add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
