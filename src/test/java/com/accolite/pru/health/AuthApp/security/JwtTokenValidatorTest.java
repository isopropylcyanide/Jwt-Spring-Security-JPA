/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.cache.LoggedOutJwtTokenCache;
import com.accolite.pru.health.AuthApp.event.OnUserLogoutSuccessEvent;
import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtTokenValidatorTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 2500;

    @Mock
    private LoggedOutJwtTokenCache loggedOutTokenCache;

    private JwtTokenProvider tokenProvider;

    private JwtTokenValidator tokenValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.tokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
        this.tokenValidator = new JwtTokenValidator(jwtSecret, loggedOutTokenCache);
    }

    @Test
    void testValidateTokenThrowsExceptionWhenTokenIsDamaged() {
        String token = tokenProvider.generateTokenFromUserId(100L);
        OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U1", token);
        when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);

        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> tokenValidator.validateToken(token + "-Damage"));
        assertTrue(ex.getMessage().contains("Incorrect signature"));
    }

    @Test
    void testValidateTokenThrowsExceptionWhenTokenIsExpired() throws InterruptedException {
        String token = tokenProvider.generateTokenFromUserId(123L);
        TimeUnit.MILLISECONDS.sleep(jwtExpiryInMs);
        OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U1", token);
        when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);

        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> tokenValidator.validateToken(token));
        assertTrue(ex.getMessage().contains("Token expired. Refresh required"));
    }

    @Test
    void testValidateTokenThrowsExceptionWhenItIsPresentInTokenCache() {
        String token = tokenProvider.generateTokenFromUserId(124L);
        OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U2", token);
        when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);

        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> tokenValidator.validateToken(token));
        assertTrue(ex.getMessage().contains("Token corresponds to an already logged out user [U2]"));
    }

    @Test
    void testValidateTokenWorksWhenItIsNotPresentInTokenCache() {
        String token = tokenProvider.generateTokenFromUserId(100L);
        tokenValidator.validateToken(token);
        verify(loggedOutTokenCache, times(1)).getLogoutEventForToken(token);
    }

    private OnUserLogoutSuccessEvent stubLogoutEvent(String email, String token) {
        return new OnUserLogoutSuccessEvent(email, token, null);
    }
}
