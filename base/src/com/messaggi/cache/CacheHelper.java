package com.messaggi.cache;

import java.lang.reflect.Constructor;

import javax.naming.InitialContext;

public class CacheHelper
{
    @SuppressWarnings("unchecked")
    static <T extends Object> T createInstance(String cacheImplClassJNDIName) throws Exception
    {
        String cacheImplClassName = (String) InitialContext.doLookup(cacheImplClassJNDIName);
        Class<?> cacheImplClazz = Class.forName(cacheImplClassName);
        Constructor<?> ctor = cacheImplClazz.getDeclaredConstructor(new Class[0]);
        ctor.setAccessible(true);
        return (T) ctor.newInstance();
    }
}

