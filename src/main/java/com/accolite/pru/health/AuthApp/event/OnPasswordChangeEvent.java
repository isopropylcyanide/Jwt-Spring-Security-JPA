package com.accolite.pru.health.AuthApp.event;

import org.springframework.context.ApplicationEvent;

public class OnPasswordChangeEvent extends ApplicationEvent {
	private String mail;
	private String action;
	private String actionStatus;

	public OnPasswordChangeEvent(String mail, String action, String actionStatus) {
		super(mail);
		this.mail = mail;
		this.action = action;
		this.actionStatus = actionStatus;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
