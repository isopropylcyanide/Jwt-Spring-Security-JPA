package com.accolite.pru.health.AuthApp.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	@Query(value = "SELECT t.expiry_time FROM password_reset_token t where t.token_name = :token", nativeQuery = true)
	Instant findExpiryTimeByToken(@Param("token") String token);

	Boolean existsByToken(String token);

	@Query(value = "SELECT t.user_id FROM password_reset_token t where t.token_name = :token", nativeQuery = true)
	Long findUserIdByToken(@Param("token") String token);

	@Query(value = "select * from password_reset_token t where t.token_name = :token", nativeQuery = true)
	PasswordResetToken findByToken(@Param("token") String token);
}