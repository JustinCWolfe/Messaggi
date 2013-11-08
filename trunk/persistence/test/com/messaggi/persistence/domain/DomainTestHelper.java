package com.messaggi.persistence.domain;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class DomainTestHelper
{
    public static User generateRandomUser(Locale locale)
    {
        Random r = new Random();

        int userSuffix = r.nextInt(10000000);
        User user = new User();
        String name = "User" + userSuffix;
        user.setName(name);
        user.setEmail(name + "@yahoo.com");

        int areaCode = r.nextInt(999);
        if (areaCode < 100) {
            areaCode = 100;
        }
        int exchange = r.nextInt(999);
        if (exchange < 100) {
            exchange = 100;
        }
        int subscriberNumber = r.nextInt(9999);
        user.setPhone(String.format("%d-%d-%d", areaCode, exchange, subscriberNumber));
        user.setPhoneParsed(String.format("%d%d%d", areaCode, exchange, subscriberNumber));

        user.setPassword(UUID.randomUUID().toString());
        user.setLocale(locale);
        user.setActive(true);

        return user;
    }
}

