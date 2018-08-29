package com.accolite.pru.health.AuthApp.event.listener;

import java.io.IOException;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.accolite.pru.health.AuthApp.event.OnPasswordChangeEvent;
import com.accolite.pru.health.AuthApp.exception.MailSendException;
import com.accolite.pru.health.AuthApp.service.MailService;

import freemarker.template.TemplateException;

@Component
public class OnPasswordChangeEventListener implements ApplicationListener<OnPasswordChangeEvent> {
	@Autowired
	private MailService mailService;

	private static final Logger logger = Logger.getLogger(OnPasswordChangeEvent.class);

	/**
	 * As soon as password is changed ,this event will be published
	 */
	@Override
	@Async
	public void onApplicationEvent(OnPasswordChangeEvent onPasswordChangeEvent) {
		sendPasswordChageAcknowledgement(onPasswordChangeEvent);
	}

	/**
	 * Sends a acknowledgement mail to the user indicating password is changed
	 * successfully
	 */
	private void sendPasswordChageAcknowledgement(OnPasswordChangeEvent event) {
		String recipientAddress = event.getMail();
		String action = event.getAction();
		String actionStatus = event.getActionStatus();

		try {
			mailService.sendAccountChangeEmail(action, actionStatus, recipientAddress);
		} catch (IOException | TemplateException | MessagingException e) {
			logger.error(e);
			throw new MailSendException(recipientAddress, "Password Change Mail");
		}
	}

}
