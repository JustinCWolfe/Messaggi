package com.messaggi.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;

public class EncodeHelper
{
    public static byte[] encodeBase64Image(String stringToEncode)
    {
        return Base64.encodeBase64(StringUtils.getBytesUtf8(stringToEncode));
    }

    public static String decodeBase64Image(byte[] bytesToDecode)
    {
        return StringUtils.newStringUtf8(Base64.decodeBase64(bytesToDecode));
    }
}

