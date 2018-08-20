package com.accolite.pru.health.AuthApp.model;


import com.accolite.pru.health.AuthApp.model.audit.DateAudit;
import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "USER")
public class User extends DateAudit {

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", allocationSize = 1)
	private Long id;

	@NaturalId
	@Column(name = "EMAIL", unique = true)
	@NotBlank
	private String email;

	@Column(name = "USERNAME", unique = true)
	@NotBlank
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "IS_ACTIVE", nullable = false)
	@NullOrNotBlank
	private Boolean active;


	@ManyToMany(fetch = FetchType.EAGER, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "USER_AUTHORITY",
			joinColumns = {
					@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
			},
			inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
			})
	private Set<Role> roles = new HashSet<>();

	public void addRole(Role role) {
		roles.add(role);
		role.getUserList().add(this);
	}

	public void addRoles(Set<Role> roles) {
		roles.forEach(this::addRole);
	}

	public void removeRole(Role role) {
		roles.remove(role);
		role.getUserList().remove(this);
	}

	public User() {
	}

	public User(User user) {
		id = user.getId();
		userName = user.getUserName();
		password = user.getPassword();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		active = user.getActive();
		roles = user.getRoles();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> authorities) {
		roles = authorities;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", active=" + active +
				", roles=" + roles +
				'}';
	}
}
