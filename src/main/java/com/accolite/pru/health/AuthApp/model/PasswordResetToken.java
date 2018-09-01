package com.accolite.pru.health.AuthApp.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetToken {

	@Id
	@Column(name = "TOKEN_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pwd_reset_token_seq")
	@SequenceGenerator(name = "pwd_reset_token_seq", allocationSize = 1)
	private Long id;

	@Column(name = "TOKEN_NAME", nullable = false, unique = true)
	private String token;

	@Column(name = "EXPIRY_TIME", nullable = false)
	private Instant expiryTime;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "USER_ID")
	private User user;

	public Instant getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Instant expiryDate) {
		this.expiryTime = expiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}