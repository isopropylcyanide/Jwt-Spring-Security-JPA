package com.accolite.pru.health.AuthApp.event.listener;

import com.accolite.pru.health.AuthApp.cache.LoggedOutJwtTokenCache;
import com.accolite.pru.health.AuthApp.event.OnUserLogoutSuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {

    private final LoggedOutJwtTokenCache tokenCache;

    @Autowired
    public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public void onApplicationEvent(OnUserLogoutSuccessEvent event) {

        String token = event.getToken();
        //here we need to set the token in the cache
    }
}
