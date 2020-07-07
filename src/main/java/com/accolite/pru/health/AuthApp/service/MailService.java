/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.model.Mail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
import java.util.concurrent.TimeUnit;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    private final Configuration templateConfiguration;

    @Value("${app.velocity.templates.location}")
    private String basePackagePath;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    @Autowired
    public MailService(JavaMailSender mailSender, Configuration templateConfiguration) {
        this.mailSender = mailSender;
        this.templateConfiguration = templateConfiguration;
    }

    public void sendEmailVerification(String emailVerificationUrl, String to)
            throws IOException, TemplateException, MessagingException {
        Mail mail = new Mail();
        mail.setSubject("Email Verification [Team CEP]");
        mail.setTo(to);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", to);
        mail.getModel().put("userEmailTokenVerificationLink", emailVerificationUrl);

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("email-verification.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

    /**
     * Setting the mail parameters.Send the reset link to the respective user's mail
     */
    public void sendResetLink(String resetPasswordLink, String to)
            throws IOException, TemplateException, MessagingException {
        Long expirationInMinutes = TimeUnit.MILLISECONDS.toMinutes(expiration);
        String expirationInMinutesString = expirationInMinutes.toString();
        Mail mail = new Mail();
        mail.setSubject("Password Reset Link [Team CEP]");
        mail.setTo(to);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", to);
        mail.getModel().put("userResetPasswordLink", resetPasswordLink);
        mail.getModel().put("expirationTime", expirationInMinutesString);

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("reset-link.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

    /**
     * Send an email to the user indicating an account change event with the correct
     * status
     */
    public void sendAccountChangeEmail(String action, String actionStatus, String to)
            throws IOException, TemplateException, MessagingException {
        Mail mail = new Mail();
        mail.setSubject("Account Status Change [Team CEP]");
        mail.setTo(to);
        mail.setFrom(mailFrom);
        mail.getModel().put("userName", to);
        mail.getModel().put("action", action);
        mail.getModel().put("actionStatus", actionStatus);

        templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
        Template template = templateConfiguration.getTemplate("account-activity-change.ftl");
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
        mail.setContent(mailContent);
        send(mail);
    }

    /**
     * Sends a simple mail as a MIME Multipart message
     */
    public void send(Mail mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(mail.getTo());
        helper.setText(mail.getContent(), true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        mailSender.send(message);
    }

}
