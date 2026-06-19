package org.example.sigfl_backend.mail.infrastructure;

import jakarta.mail.internet.MimeMessage;

import org.example.sigfl_backend.mail.domain.MailSender;
import org.example.sigfl_backend.mail.domain.model.EmailMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * SMTP {@link MailSender} adapter (uses Spring's {@link JavaMailSender}, which is
 * configured from {@code spring.mail.*}). Active when {@code mail.provider=smtp}.
 */
@Component
@ConditionalOnProperty(name = "mail.provider", havingValue = "smtp")
public class SmtpMailSender implements MailSender {

    private final JavaMailSender javaMailSender;
    private final String from;

    public SmtpMailSender(JavaMailSender javaMailSender, MailProperties properties) {
        this.javaMailSender = javaMailSender;
        this.from = properties.getFrom();
    }

    @Override
    public void send(EmailMessage message) {
        try {
            MimeMessage mime = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, false, "UTF-8");
            helper.setFrom(from);
            helper.setTo(message.to().toArray(String[]::new));
            helper.setSubject(message.subject());
            helper.setText(message.body(), message.html());
            javaMailSender.send(mime);
        } catch (jakarta.mail.MessagingException | MailException e) {
            throw new IllegalStateException("Failed to send email: " + message.subject(), e);
        }
    }
}
