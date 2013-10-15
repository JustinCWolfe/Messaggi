package com.messaggi.persistence.dao;

// Generated Oct 15, 2013 1:21:19 PM by Hibernate Tools 4.0.0

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.messaggi.persistence.domain.ApplicationPlatformKey;

/**
 * Home object for domain model class ApplicationPlatformKey.
 * @see com.messaggi.persistence.dao.ApplicationPlatformKey
 * @author Hibernate Tools
 */
public class ApplicationPlatformKeyHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformKeyHome.class);

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

    public void persist(ApplicationPlatformKey transientInstance)
    {
        log.debug("persisting ApplicationPlatformKey instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(ApplicationPlatformKey instance)
    {
        log.debug("attaching dirty ApplicationPlatformKey instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(ApplicationPlatformKey instance)
    {
        log.debug("attaching clean ApplicationPlatformKey instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(ApplicationPlatformKey persistentInstance)
    {
        log.debug("deleting ApplicationPlatformKey instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ApplicationPlatformKey merge(ApplicationPlatformKey detachedInstance)
    {
        log.debug("merging ApplicationPlatformKey instance");
        try {
            ApplicationPlatformKey result = (ApplicationPlatformKey) sessionFactory.getCurrentSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ApplicationPlatformKey findById(java.lang.String id)
    {
        log.debug("getting ApplicationPlatformKey instance with id: " + id);
        try {
            ApplicationPlatformKey instance = (ApplicationPlatformKey) sessionFactory.getCurrentSession().get(
                    "com.messaggi.persistence.dao.ApplicationPlatformKey", id);
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

    public List<ApplicationPlatformKey> findByExample(ApplicationPlatformKey instance)
    {
        log.debug("finding ApplicationPlatformKey instance by example");
        try {
            List<ApplicationPlatformKey> results = sessionFactory.getCurrentSession()
                    .createCriteria("com.messaggi.persistence.dao.ApplicationPlatformKey").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
