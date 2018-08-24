package com.accolite.pru.health.AuthApp.model;

public enum TokenStatus {
	/**
	 * Token is in pending state awaiting user confirmation
	 */
	STATUS_PENDING,

	/**
	 * Token has been confirmed successfully by the user
	 */
	STATUS_CONFIRMED
}
