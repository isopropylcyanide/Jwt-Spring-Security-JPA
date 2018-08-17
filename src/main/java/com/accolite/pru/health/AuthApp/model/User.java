package com.accolite.pru.health.AuthApp.model;


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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "USER")
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    @NotNull
    private String userName;

    @Column(name = "PASSWORD", nullable = false)
    @NotNull
    private String password;

    @Column(name = "FIRST_NAME", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false)
    @NotNull
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @NotNull
    private String email;

    @Column(name = "IS_ACTIVE", nullable = false)
    @NotNull
    private Boolean active;

    @Column(name = "LAST_ISSUE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastIssuedDate;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {
    				CascadeType.PERSIST,
					CascadeType.MERGE
    			})
    @JoinTable(name = "USER_AUTHORITY",
                joinColumns = {
                        @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
                },
                inverseJoinColumns = {
                        @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITY_ID")
                })
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role){
    	this.roles.add(role);
    	role.getUserList().add(this);
	}

	public void addRoles(Set<Role> roles){
    	roles.forEach(this::addRole);
	}

	public void removeRole(Role role){
    	this.roles.remove(role);
    	role.getUserList().remove(this);
	}

	public User(){
	}

	public User(User user) {
		this.id = user.getId();
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.active = user.getActive();
		this.lastIssuedDate = user.getLastIssuedDate();
		this.roles = user.getRoles();
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

    public Date getLastIssuedDate() {
        return lastIssuedDate;
    }

    public void setLastIssuedDate(Date lastIssuedDate) {
        this.lastIssuedDate = lastIssuedDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> authorities) {
        this.roles = authorities;
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
				", lastIssuedDate=" + lastIssuedDate +
				", roles=" + roles +
				'}';
	}
}
