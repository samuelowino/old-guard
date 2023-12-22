package com.oldguard.rickandmortyapi.utils;


public final class Utilities {

    public static final String API_URI = "/api/v1/";

    public static String getNextURL(String domain, String page, String name, String status) {

        return String.format("%s/?page=%d&name=%s&status=%s", domain, Integer.parseInt(page) + 2, name, status);

    }

    public static String getPrevURL(String domain, String page, String name, String status) {

        return String.format("%s/?page=%d&name=%s&status=%s", domain, Integer.parseInt(page) - 2, name, status);

    }
}
