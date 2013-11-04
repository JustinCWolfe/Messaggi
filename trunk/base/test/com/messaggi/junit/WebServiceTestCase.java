package com.messaggi.junit;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.catalina.startup.Bootstrap;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class WebServiceTestCase extends MessaggiTestCase
{
    private static final String CATALINA_HOME = "C:/apache-tomcat-7.0.42";

    private static final String CATALINA_BIN = CATALINA_HOME + "/bin";

    private static Bootstrap tomcatBootstrap;

    @SuppressWarnings("rawtypes")
    private static Method getAddUrlMethod() throws Exception
    {
        Class[] params = { URL.class };
        Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", params);
        addURLMethod.setAccessible(true);
        return addURLMethod;
    }

    // We need to load the tomcat-juli.jar class dynamically because the Bootstrap class below depends
    // on it.  This is unnecessary when startup tomcat via startup scripts because those scripts put 
    // both bootstrap.jar and tomcat-juli.jar onto the CLASSPATH prior to starting tomcat.
    private static void updateClassLoader() throws Exception
    {
        Method addUrlMethod = getAddUrlMethod();
        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        File bootstrapJar = new File(CATALINA_BIN + "/bootstrap.jar");
        addUrlMethod.invoke(classLoader, new Object[] { bootstrapJar.toURI().toURL() });
        File tomcatJuliJar = new File(CATALINA_BIN + "/tomcat-juli.jar");
        addUrlMethod.invoke(classLoader, new Object[] { tomcatJuliJar.toURI().toURL() });
    }

    @BeforeClass
    public static void suiteSetUp() throws Exception
    {
        updateClassLoader();
        tomcatBootstrap = new Bootstrap();
        tomcatBootstrap.setCatalinaHome(CATALINA_HOME);
        tomcatBootstrap.start();
    }

    public static void suiteTearDown() throws Exception
    {
        tomcatBootstrap.stop();
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }
}

