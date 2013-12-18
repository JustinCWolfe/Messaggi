package com.messaggi.util;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBHelper
{
    public static <T> String objectToXML(T instance) throws JAXBException, IOException
    {
        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller m = context.createMarshaller();
        //for pretty-print XML in JAXB
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out for debugging
        // m.marshal(emp, System.out);

        // Write to File
        //m.marshal(instance, new File(FILE_NAME));

        try (StringWriter sw = new StringWriter()) {
            m.marshal(instance, sw);
            return sw.toString();
        }
    }
}

