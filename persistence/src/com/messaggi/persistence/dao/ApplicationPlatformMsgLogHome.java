package com.messaggi.persistence.dao;

// Generated Oct 15, 2013 1:21:19 PM by Hibernate Tools 4.0.0

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.messaggi.persistence.domain.ApplicationPlatformMsgLog;

/**
 * Home object for domain model class ApplicationPlatformMsgLog.
 * @see com.messaggi.persistence.dao.ApplicationPlatformMsgLog
 * @author Hibernate Tools
 */
public class ApplicationPlatformMsgLogHome
{

    private static final Log log = LogFactory.getLog(ApplicationPlatformMsgLogHome.class);

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

    public void persist(ApplicationPlatformMsgLog transientInstance)
    {
        log.debug("persisting ApplicationPlatformMsgLog instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(ApplicationPlatformMsgLog instance)
    {
        log.debug("attaching dirty ApplicationPlatformMsgLog instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(ApplicationPlatformMsgLog instance)
    {
        log.debug("attaching clean ApplicationPlatformMsgLog instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(ApplicationPlatformMsgLog persistentInstance)
    {
        log.debug("deleting ApplicationPlatformMsgLog instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ApplicationPlatformMsgLog merge(ApplicationPlatformMsgLog detachedInstance)
    {
        log.debug("merging ApplicationPlatformMsgLog instance");
        try {
            ApplicationPlatformMsgLog result = (ApplicationPlatformMsgLog) sessionFactory.getCurrentSession().merge(
                    detachedInstance);
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
            ApplicationPlatformMsgLog instance = (ApplicationPlatformMsgLog) sessionFactory.getCurrentSession().get(
                    "com.messaggi.persistence.dao.ApplicationPlatformMsgLog", id);
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

    public List<ApplicationPlatformMsgLog> findByExample(ApplicationPlatformMsgLog instance)
    {
        log.debug("finding ApplicationPlatformMsgLog instance by example");
        try {
            List<ApplicationPlatformMsgLog> results = sessionFactory
                    .getCurrentSession().createCriteria("com.messaggi.persistence.dao.ApplicationPlatformMsgLog")
                    .add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
