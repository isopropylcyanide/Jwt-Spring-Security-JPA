package com.accolite.pru.health.AuthApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class PasswordResetLinkException extends RuntimeException {

    private final String user;
    private final String message;

    public PasswordResetLinkException(String user, String message) {
        super(String.format("Failed to send password reset link to User[%s] : '%s'", user, message));
        this.user = user;
        this.message = message;
    }
}