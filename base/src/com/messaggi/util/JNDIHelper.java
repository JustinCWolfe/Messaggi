package com.messaggi.util;

import java.lang.reflect.Constructor;

import javax.naming.InitialContext;

public class JNDIHelper
{
    @SuppressWarnings("unchecked")
    public static <T extends Object> T createInstance(String implClassJNDIName) throws Exception
    {
        String implClassName = (String) InitialContext.doLookup(implClassJNDIName);
        Class<?> implClazz = Class.forName(implClassName);
        Constructor<?> ctor = implClazz.getDeclaredConstructor(new Class[0]);
        ctor.setAccessible(true);
        return (T) ctor.newInstance();
    }
}

