package com.messaggi.dao.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil
{
    private static final Log log = LogFactory.getLog(HibernateUtil.class);

    private static final File rootDirectory = new File(System.getProperty("user.dir"));

    private static final File mappingFilesDirectory = new File(rootDirectory, "dao/mappings");

    private static final File configurationFile = new File(rootDirectory, "database/hibernate.cfg.xml");

    private static final SessionFactory sessionFactory;

    private static final ServiceRegistry serviceRegistry;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.addDirectory(mappingFilesDirectory);
            configuration = configuration.configure(configurationFile);
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
                    .buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException ex) {
            String msg = "Initial SessionFactory creation failed.";
            log.error(msg, ex);
            System.err.println(msg + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
}

