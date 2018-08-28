package com.accolite.pru.health.AuthApp.event.listener;

import java.io.IOException;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.accolite.pru.health.AuthApp.event.OnGenerateResetLinkMailEvent;
import com.accolite.pru.health.AuthApp.exception.MailSendException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.service.MailService;

import freemarker.template.TemplateException;

@Component
public class OnGenerateResetLinkMailEventListener implements ApplicationListener<OnGenerateResetLinkMailEvent> {
	@Autowired
	private MailService mailService;

	private static final Logger logger = Logger.getLogger(OnGenerateResetLinkMailEventListener.class);

	/**
	 * As soon as a registration event is complete, invoke the email verification
	 */
	@Override
	@Async
	public void onApplicationEvent(OnGenerateResetLinkMailEvent onGenerateResetLinkMailEvent) {
		sendResetLink(onGenerateResetLinkMailEvent);
	}

	/**
	 * Send email verification to the passwordResetToken and persist the token in
	 * the database.
	 */
	private void sendResetLink(OnGenerateResetLinkMailEvent event) {
		PasswordResetToken passwordResetToken = event.getPasswordResetToken();
		String emailVerificationToken = event.getToken();
		String recipientAddress = passwordResetToken.getEmail();
		String emailConfirmationUrl = event.getRedirectUrl().queryParam("token", passwordResetToken.getToken())
				.toUriString();
		try {
			mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
		} catch (IOException | TemplateException | MessagingException e) {
			logger.error(e);
			throw new MailSendException(recipientAddress, "Email Verification");
		}
	}

}
