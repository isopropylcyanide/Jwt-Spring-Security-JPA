package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.Mail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration templateConfiguration;

	@Value("${spring.mail.username}")
	private String mailFrom;

	private static final Logger logger = Logger.getLogger(MailService.class);

	public void sendEmailVerification(String emailVerificationUrl, String to) throws IOException,
			TemplateException, MessagingException {
		Mail mail = new Mail();
		mail.setSubject("Email Verification [Team CEP]");
		mail.setTo(to);
		mail.setFrom(mailFrom);
		mail.getModel().put("userName", to);
		mail.getModel().put("userEmailTokenVerificationLink", emailVerificationUrl);

		templateConfiguration.setClassForTemplateLoading(getClass(), "/templates/");
		Template t = templateConfiguration.getTemplate("email-verification.ftl");
		String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
		mail.setContent(mailContent);
		send(mail);
	}

	/**
	 * Send an email to the user indicating an account change event with the correct status
	 */
	public void sendAccountChangeEmail(String action, String actionStatus, String to) throws IOException,
			TemplateException, MessagingException {
		Mail mail = new Mail();
		mail.setSubject("Account Status Change [Team CEP]");
		mail.setTo(to);
		mail.setFrom(mailFrom);
		mail.getModel().put("userName", to);
		mail.getModel().put("action", action);
		mail.getModel().put("actionStatus", actionStatus);

		templateConfiguration.setClassForTemplateLoading(getClass(), "/templates/");
		Template t = templateConfiguration.getTemplate("account-activity-change.ftl");
		String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
		mail.setContent(mailContent);
		send(mail);
	}

	/**
	 * Sends a simple mail as a MIME Multipart message
	 */
	public void send(Mail mail) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setTo(mail.getTo());
		helper.setText(mail.getContent(), true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		logger.info("Sending mail service in thread: " + Thread.currentThread());
		mailSender.send(message);
	}

}
