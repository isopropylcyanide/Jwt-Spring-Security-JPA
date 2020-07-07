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

import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JwtTokenProviderTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 2500;

    private JwtTokenProvider tokenProvider;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.tokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
    }

    @Test
    public void testGetUserIdFromJWT() {
        String token = tokenProvider.generateToken(stubCustomUser());
        assertEquals(100, tokenProvider.getUserIdFromJWT(token).longValue());
    }

    @Test
    public void testGetTokenExpiryFromJWT() {
        String token = tokenProvider.generateTokenFromUserId(120L);
        assertNotNull(tokenProvider.getTokenExpiryFromJWT(token));
    }

    @Test
    public void testGetExpiryDuration() {
        assertEquals(jwtExpiryInMs, tokenProvider.getExpiryDuration());
    }

    private CustomUserDetails stubCustomUser() {
        User user = new User();
        user.setId((long) 100);
        return new CustomUserDetails(user);
    }
}
