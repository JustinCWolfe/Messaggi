package com.messaggi.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class JAXBHelper
{
    private static <T> Marshaller getJAXBMarshaller(T instance) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller marshaller = context.createMarshaller();
        //for pretty-print XML in JAXB
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-16");
        return marshaller;
    }

    public static <T> String objectToJSON(T instance) throws Exception
    {
        /*
         * ObjectMapper om = new ObjectMapper();
         * mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
         * false); if (pretty) { return
         * mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj); }
         * return mapper.writeValueAsString(obj);
         */
        return null;
    }

    public static <T> String objectToXML(T instance) throws Exception
    {
        Marshaller marshaller = getJAXBMarshaller(instance);

        // Write to System.out for debugging
        // m.marshal(emp, System.out);

        // Write to File
        //m.marshal(instance, new File(FILE_NAME));

        try (StringWriter sw = new StringWriter()) {
            marshaller.marshal(instance, sw);
            return sw.toString();
        }
    }

    public static <T> String objectToXML(T[] instances) throws Exception
    {
        Marshaller marshaller = getJAXBMarshaller(instances[0]);

        // Write to System.out for debugging
        // m.marshal(emp, System.out);

        // Write to File
        //m.marshal(instance, new File(FILE_NAME));

        try (StringWriter sw = new StringWriter()) {
            for (T instance : instances) {
                marshaller.marshal(instance, sw);
                // Set JAXB_FRAGMENT to true so subsequent objects will be serialized without the XML header.
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            }
            return sw.toString();
        }
    }
}

