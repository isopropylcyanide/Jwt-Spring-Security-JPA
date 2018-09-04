package com.accolite.pru.health.AuthApp.repository;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<Instant> findExpiryDateByToken(String token);

	Boolean existsByToken(String token);

	Optional<User> findUserByToken(String token);

	Optional<PasswordResetToken> findByToken(String token);
}