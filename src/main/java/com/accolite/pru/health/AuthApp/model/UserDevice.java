package com.accolite.pru.health.AuthApp.model;

import com.accolite.pru.health.AuthApp.model.audit.DateAudit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "USER_DEVICE")
public class UserDevice extends DateAudit {

	@Id
	@Column(name = "USER_DEVICE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_device_seq")
	@SequenceGenerator(name = "user_device_seq", allocationSize = 1)
	private Long id;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@Column(name = "DEVICE_TYPE")
	@Enumerated(value = EnumType.STRING)
	private DeviceType deviceType;

	@Column(name = "NOTIFICATION_TOKEN")
	private String notificationToken;

	@Column(name = "DEVICE_ID")
	private String deviceId;

	public UserDevice() {
	}

	public UserDevice(Long id, User user, DeviceType deviceType, String notificationToken, String deviceId) {
		this.id = id;
		this.user = user;
		this.deviceType = deviceType;
		this.notificationToken = notificationToken;
		this.deviceId = deviceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getNotificationToken() {
		return notificationToken;
	}

	public void setNotificationToken(String notificationToken) {
		this.notificationToken = notificationToken;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
