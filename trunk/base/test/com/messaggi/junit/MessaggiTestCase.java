package com.messaggi.junit;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import net.sourceforge.jtds.jdbc.Driver;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;

import org.apache.catalina.startup.Bootstrap;
import org.apache.catalina.startup.Tomcat;
import org.junit.BeforeClass;

import com.messaggi.dao.persist.PersistManager;

public abstract class MessaggiTestCase extends MessaggiLogicTestCase
{
    private static final String DEFFAULT_CATALINA_HOME = "C:/apache-tomcat-7.0.42";

    private static Bootstrap tomcatBootstrap;

    private static Tomcat tomcatEmbedded;

    private static String getCatalinaHome()
    {
        String catalinaHomeEnvVar = System.getenv("CATALINA_HOME");
        return (catalinaHomeEnvVar != null) ? catalinaHomeEnvVar : DEFFAULT_CATALINA_HOME;
    }
    
    private static String getCatalinaBin()
    {
        return getCatalinaHome() + "/bin";
    }

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
    private static boolean classLoaderUpdated = false;
    private static void updateClassLoader() throws Exception
    {
        if (classLoaderUpdated) {
            return;
        }
        Method addUrlMethod = getAddUrlMethod();
        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        File bootstrapJar = new File(getCatalinaBin() + "/bootstrap.jar");
        addUrlMethod.invoke(classLoader, new Object[] { bootstrapJar.toURI().toURL() });
        File tomcatJuliJar = new File(getCatalinaBin() + "/tomcat-juli.jar");
        addUrlMethod.invoke(classLoader, new Object[] { tomcatJuliJar.toURI().toURL() });
        classLoaderUpdated = true;
    }

    /**
     * Note that this method will not work if the context has already been setup
     * by the tomcat bootstrap process.
     * 
     * @throws Exception
     */
    public static void setupTestingContext() throws Exception
    {
        updateClassLoader();
        // Create initial context
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext ic = new InitialContext();

        ic.createSubcontext("java:");
        ic.createSubcontext("java:/comp");
        ic.createSubcontext("java:/comp/env");
        ic.createSubcontext("java:/comp/env/jdbc");
        
        // Construct DataSource
        Properties p = new Properties();
        p.load(new FileInputStream("unittest.properties"));
        String dbServer = p.getProperty("db.server");
        String dbInstance = p.getProperty("db.instance");
        String dbDatabase = p.getProperty("db.database");
        String dbUser = p.getProperty("db.user");
        String dbPwd = p.getProperty("db.pwd");

        JtdsDataSource ds = new JtdsDataSource();
        ds.setServerName(dbServer);
        ds.setServerType(Driver.SQLSERVER);
        ds.setInstance(dbInstance);
        ds.setDatabaseName(dbDatabase);
        ds.setUser(dbUser);
        ds.setPassword(dbPwd);
        ds.setDescription("Messaggi SqlServer(jTDS) Unit Testing DataSource");
        ic.bind(PersistManager.MESSAGGI_DATABASE_JNDI_NAME, ds);

        // Implementation class names for caches.
        ic.bind("java:/comp/env/ApplicationPlatformTokensCacheImpl", "com.messaggi.cache.ApplicationPlatformTokensImpl");
        ic.bind("java:/comp/env/ApplicationPlatformsCacheImpl", "com.messaggi.cache.ApplicationPlatformsImpl");
        ic.bind("java:/comp/env/ApplicationPlatformDevicessCacheImpl",
                "com.messaggi.cache.ApplicationPlatformDevicesImpl");
    }

    public static void startTomcat() throws Exception
    {
        updateClassLoader();
        tomcatBootstrap = new Bootstrap();
        tomcatBootstrap.setCatalinaHome(getCatalinaHome());
        tomcatBootstrap.start();
    }

    public static void stopTomcat() throws Exception
    {
        if (tomcatBootstrap != null) {
            tomcatBootstrap.stop();
        }
    }

    /**
     * This should be used when you want tomcat setup for your webapp but don't
     * want it mucking with jdni naming (ex. in the case where you want to add
     * jndi references for testing - like for jdbc).
     * 
     * @throws Exception
     */
    public static void startTomcatEmbedded() throws Exception
    {
        updateClassLoader();
        tomcatEmbedded = new Tomcat();
        tomcatEmbedded.setBaseDir(getCatalinaHome());
        tomcatEmbedded.addWebapp("/messaggi", getCatalinaHome() + "\\webapps\\messaggi");
        tomcatEmbedded.start();
    }

    public static void stopTomcatEmbedded() throws Exception
    {
        if (tomcatEmbedded != null) {
            tomcatEmbedded.stop();
        }
    }

    @BeforeClass
    public static void suiteSetUp() throws Exception
    {
        startTomcatEmbedded();
        setupTestingContext();
    }

    public static void suiteTearDown() throws Exception
    {
        stopTomcatEmbedded();
    }
}

