package com.accolite.pru.health.AuthApp.util;

import java.util.UUID;

public class Util {

    /**
     * Returns true if a value is true. Useful for method references
     */
    public static Boolean isTrue(Boolean value) {
        return value;
    }

    /**
     * Generate a random UUID
     */
    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
