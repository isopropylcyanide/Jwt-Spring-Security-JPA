package com.accolite.pru.health.AuthApp.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.beans.factory.annotation.Value;

import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;

@Entity(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetToken {

	@Id
	@Column(name = "TOKEN_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
	@SequenceGenerator(name = "token_seq", allocationSize = 1)
	private Long id;

	@Column(name = "TOKEN_NAME", nullable = false, unique = true)
	private String token;

	@Column(name = "EXPIRY_TIME", nullable = false)
	private Timestamp expiryTime;

	@Column(name = "EMAIL")
	@NullOrNotBlank(message = "Email cannot be blank")
	private String email;

	@Column(name = "EXPIRATION")
	@Value("${app.token.expiration}")
	private long expiration;

	public Timestamp getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Timestamp expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

}