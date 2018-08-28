package com.accolite.pru.health.AuthApp.repository;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accolite.pru.health.AuthApp.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

	@Query(value = "SELECT t.email FROM password_reset_token t where t.token_name = :token", nativeQuery = true)
	String findEmailByToken(@Param("token") String token);

	@Query(value = "SELECT t.expiry_time FROM password_reset_token t where t.token_name = :token", nativeQuery = true)
	Timestamp findExpiryTimeByToken(@Param("token") String token);

}