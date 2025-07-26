package com.soumya.urlshortener.util;

import eu.bitwalker.useragentutils.UserAgent;

public class UserAgentUtil {

    public static UserAgent getUserAgent(String header){
        return UserAgent.parseUserAgentString(header);
    }

    public static String extractOperatingSystem(UserAgent userAgent){
        return userAgent.getOperatingSystem().getName();
    }

    public static String extractDevice(UserAgent userAgent){
        return userAgent.getOperatingSystem().getDeviceType().getName();
    }

    public static String extractBrowser(UserAgent userAgent){
        return userAgent.getBrowser().getName();
    }
}
