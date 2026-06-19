package bj.mef.sigfl_backend.mail.application;

import bj.mef.sigfl_backend.mail.domain.MailSender;
import bj.mef.sigfl_backend.mail.domain.model.EmailMessage;
import org.springframework.stereotype.Service;

/**
 * Application service for sending email. Other modules depend on this rather
 * than on a concrete transport; the active {@link MailSender} adapter (SMTP or
 * log) is chosen by configuration.
 */
@Service
public class MailService {

    private final MailSender mailSender;

    public MailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(EmailMessage message) {
        mailSender.send(message);
    }

    public void sendText(String to, String subject, String body) {
        mailSender.send(EmailMessage.text(to, subject, body));
    }

    public void sendHtml(String to, String subject, String html) {
        mailSender.send(EmailMessage.html(to, subject, html));
    }
}
