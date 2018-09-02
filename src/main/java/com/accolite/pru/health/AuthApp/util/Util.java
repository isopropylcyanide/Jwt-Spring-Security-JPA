package com.accolite.pru.health.AuthApp.util;

import java.util.UUID;

public class Util {

	/**
	 * Returns true if a value is true. Useful for method references
	 */
	public static Boolean isTrue(Boolean value) {
		return true == value;
	}

	/**
	 * Returns false if a value is false. Useful for method references
	 */
	public static Boolean isFalse(Boolean value) {
		return false == value;
	}

	/**
	 * Generate a random UUID
	 */
	public static String generateRandomUuid() {
		return UUID.randomUUID().toString();
	}
}
