package com.example.teamcity.api.locator;

public final class Locator {


    private Locator() {
    }

    public static String byName(String name) {
        return "name:" + name;
    }

    public static String byId(String id) {
        return "id:" + id;
    }
}

