package com.messaggi.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class JAXBHelper
{
    public static <T> String objectToXML(T instance) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller m = context.createMarshaller();
        //for pretty-print XML in JAXB
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-16");

        // Write to System.out for debugging
        // m.marshal(emp, System.out);

        // Write to File
        //m.marshal(instance, new File(FILE_NAME));

        try (StringWriter sw = new StringWriter()) {
            m.marshal(instance, sw);
            return sw.toString();
        }
    }

    public static <T> String objectToXML(T[] instances) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(instances[0].getClass());
        Marshaller m = context.createMarshaller();
        //for pretty-print XML in JAXB
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-16");

        // Write to System.out for debugging
        // m.marshal(emp, System.out);

        // Write to File
        //m.marshal(instance, new File(FILE_NAME));

        try (StringWriter sw = new StringWriter()) {
            for (T instance : instances) {
                m.marshal(instance, sw);
                // Set JAXB_FRAGMENT to true so subsequent objects will be serialized without the XML header.
                m.setProperty(Marshaller.JAXB_FRAGMENT, true);
            }
            return sw.toString();
        }
    }
}

