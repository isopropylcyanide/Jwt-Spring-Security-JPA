package com.accolite.pru.health.AuthApp.model.social;

import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.time.Instant;

@Entity(name = "FACEBOOK_LOGIN")
public class FacebookLogin extends DateAudit {

	@Id
	@Column(name = "FB_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facebook_login_seq")
	@SequenceGenerator(name = "facebook_login_seq", allocationSize = 1)
	private Long id;

	@Column(name = "TOKEN", unique = true)
	@NaturalId
	private String accessToken;

	@Column(name = "EXPIRY_DT", nullable = false)
	private Instant tokenExpiry;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "USER_ID")
	private User user;

	@Column(name = "ASSOC_EMAIL", unique = true)
	private String associatedEmail;

	public FacebookLogin() {
	}

	public FacebookLogin(Long id, String accessToken, Instant tokenExpiry, User user, String associatedEmail) {
		this.id = id;
		this.accessToken = accessToken;
		this.tokenExpiry = tokenExpiry;
		this.user = user;
		this.associatedEmail = associatedEmail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Instant getTokenExpiry() {
		return tokenExpiry;
	}

	public void setTokenExpiry(Instant tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAssociatedEmail() {
		return associatedEmail;
	}

	public void setAssociatedEmail(String associatedEmail) {
		this.associatedEmail = associatedEmail;
	}
}
