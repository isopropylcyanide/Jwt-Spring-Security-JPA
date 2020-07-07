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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtTokenValidatorTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 2500;

    @Mock
    private LoggedOutJwtTokenCache loggedOutTokenCache;

    private JwtTokenProvider tokenProvider;

    private JwtTokenValidator tokenValidator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.tokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
        this.tokenValidator = new JwtTokenValidator(jwtSecret, loggedOutTokenCache);
    }

    @Test
    public void testValidateTokenThrowsExceptionWhenTokenIsDamaged() {
        String token = tokenProvider.generateTokenFromUserId(100L);
        OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U1", token);
        when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);

        thrown.expect(InvalidTokenRequestException.class);
        thrown.expectMessage("Incorrect signature");
        tokenValidator.validateToken(token + "-Damage");
    }

    @Test
    public void testValidateTokenThrowsExceptionWhenTokenIsExpired() throws InterruptedException {
        String token = tokenProvider.generateTokenFromUserId(123L);
        TimeUnit.MILLISECONDS.sleep(jwtExpiryInMs);
        OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U1", token);
        when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);

        thrown.expect(InvalidTokenRequestException.class);
        thrown.expectMessage("Token expired. Refresh required");
        tokenValidator.validateToken(token);
    }

    @Test
    public void testValidateTokenThrowsExceptionWhenItIsPresentInTokenCache() {
        String token = tokenProvider.generateTokenFromUserId(124L);
        OnUserLogoutSuccessEvent logoutEvent = stubLogoutEvent("U2", token);
        when(loggedOutTokenCache.getLogoutEventForToken(token)).thenReturn(logoutEvent);

        thrown.expect(InvalidTokenRequestException.class);
        thrown.expectMessage("Token corresponds to an already logged out user [U2]");
        tokenValidator.validateToken(token);
    }

    @Test
    public void testValidateTokenWorksWhenItIsNotPresentInTokenCache() {
        String token = tokenProvider.generateTokenFromUserId(100L);
        tokenValidator.validateToken(token);
        verify(loggedOutTokenCache, times(1)).getLogoutEventForToken(token);
    }

    private OnUserLogoutSuccessEvent stubLogoutEvent(String email, String token) {
        return new OnUserLogoutSuccessEvent(email, token, null);
    }
}
