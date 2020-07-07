package com.accolite.pru.health.AuthApp.event;

import com.accolite.pru.health.AuthApp.model.payload.LogOutRequest;
import org.springframework.context.ApplicationEvent;

public class OnUserLogoutSuccessEvent extends ApplicationEvent {

    private final String userEmail;
    private final String token;
    private final LogOutRequest logOutRequest;

    public OnUserLogoutSuccessEvent(String userEmail, String token, LogOutRequest logOutRequest) {
        super(userEmail);
        this.userEmail = userEmail;
        this.token = token;
        this.logOutRequest = logOutRequest;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getToken() {
        return token;
    }

    public LogOutRequest getLogOutRequest() {
        return logOutRequest;
    }
}
