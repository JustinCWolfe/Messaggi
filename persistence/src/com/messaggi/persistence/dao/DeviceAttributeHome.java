package com.messaggi.persistence.dao;

// Generated Oct 15, 2013 1:21:19 PM by Hibernate Tools 4.0.0

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;

import com.messaggi.persistence.domain.DeviceAttribute;
import com.messaggi.persistence.domain.DeviceAttributeId;

/**
 * Home object for domain model class DeviceAttribute.
 * @see com.messaggi.persistence.dao.DeviceAttribute
 * @author Hibernate Tools
 */
public class DeviceAttributeHome
{

    private static final Log log = LogFactory.getLog(DeviceAttributeHome.class);

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

    public void persist(DeviceAttribute transientInstance)
    {
        log.debug("persisting DeviceAttribute instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(DeviceAttribute instance)
    {
        log.debug("attaching dirty DeviceAttribute instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(DeviceAttribute instance)
    {
        log.debug("attaching clean DeviceAttribute instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(DeviceAttribute persistentInstance)
    {
        log.debug("deleting DeviceAttribute instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public DeviceAttribute merge(DeviceAttribute detachedInstance)
    {
        log.debug("merging DeviceAttribute instance");
        try {
            DeviceAttribute result = (DeviceAttribute) sessionFactory.getCurrentSession().merge(detachedInstance);
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
            DeviceAttribute instance = (DeviceAttribute) sessionFactory.getCurrentSession().get(
                    "com.messaggi.persistence.dao.DeviceAttribute", id);
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

    public List<DeviceAttribute> findByExample(DeviceAttribute instance)
    {
        log.debug("finding DeviceAttribute instance by example");
        try {
            List<DeviceAttribute> results = sessionFactory.getCurrentSession()
                    .createCriteria("com.messaggi.persistence.dao.DeviceAttribute").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
