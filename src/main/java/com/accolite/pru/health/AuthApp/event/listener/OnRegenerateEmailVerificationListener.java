package com.accolite.pru.health.AuthApp.event.listener;

import com.accolite.pru.health.AuthApp.event.OnRegenerateEmailVerificationEvent;
import com.accolite.pru.health.AuthApp.exception.MailSendException;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.service.MailService;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class OnRegenerateEmailVerificationListener implements ApplicationListener<OnRegenerateEmailVerificationEvent> {

	@Autowired
	private MailService mailService;

	private static final Logger logger = Logger.getLogger(OnRegenerateEmailVerificationListener.class);

	/**
	 * As soon as a registration event is complete, invoke the email verification
	 */
	@Override
	@Async
	public void onApplicationEvent(OnRegenerateEmailVerificationEvent onRegenerateEmailVerificationEvent) {
		resendEmailVerification(onRegenerateEmailVerificationEvent);
	}

	/**
	 * Send email verification to the user and persist the token in the database.
	 */
	private void resendEmailVerification(OnRegenerateEmailVerificationEvent event) {
		User user = event.getUser();
		EmailVerificationToken emailVerificationToken = event.getToken();
		String recipientAddress = user.getEmail();

		String emailConfirmationUrl =
				event.getRedirectUrl().queryParam("token", emailVerificationToken.getToken()).toUriString();
		try {
			mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
		} catch (IOException | TemplateException | MessagingException e) {
			logger.error(e);
			throw new MailSendException(recipientAddress, "Email Verification");
		}
	}

}
