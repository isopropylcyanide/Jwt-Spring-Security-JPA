package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.payload.PasswordResetRequest;
import com.accolite.pru.health.AuthApp.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordResetTokenServiceTest {

    @Mock
    private PasswordResetTokenRepository repository;

    private PasswordResetTokenService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.service = new PasswordResetTokenService(repository);
    }

    @Test
    void testMatchEmailWhenTokenIsExpired() {
        PasswordResetToken token = mock(PasswordResetToken.class, Mockito.RETURNS_DEEP_STUBS);
        when(token.getExpiryDate()).thenReturn(Instant.now().minusSeconds(10));

        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> service.verifyExpiration(token));
        assertTrue(ex.getMessage().contains("Expired token. Please issue a new request"));
    }

    @Test
    void testMatchEmailWhenEmailMatches() {
        PasswordResetToken token = mock(PasswordResetToken.class, Mockito.RETURNS_DEEP_STUBS);
        when(token.getUser().getEmail()).thenReturn("email-1");

        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("email-1");
        assertAll(() -> service.matchEmail(token, request.getEmail()));
    }


    @Test
    void testMatchEmailWhenEmailDoesNotMatch() {
        PasswordResetToken token = mock(PasswordResetToken.class, Mockito.RETURNS_DEEP_STUBS);
        when(token.getUser().getEmail()).thenReturn("email-1");

        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("email-2");
        InvalidTokenRequestException ex = assertThrows(InvalidTokenRequestException.class,
                () -> service.matchEmail(token, request.getEmail()));
        assertTrue(ex.getMessage().contains("Token is invalid for the given user email-2"));
    }
}
