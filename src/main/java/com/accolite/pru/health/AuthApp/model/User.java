package com.accolite.pru.health.AuthApp.model;


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
import java.util.List;

@Entity(name = "USER")
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
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

    @Column(name = "EMAIL", nullable = false)
    @NotNull
    private String email;

    @Column(name = "IS_ACTIVE", nullable = false)
    @NotNull
    private Boolean active;

    @Column(name = "LAST_ISSUE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastIssuedDate;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_AUTHORITY",
                joinColumns = {
                        @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
                },
                inverseJoinColumns = {
                        @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITY_ID")
                })
    private List<Authority> authorities;

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

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
