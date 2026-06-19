package org.example.sigfl_backend.mail.infrastructure;

import org.example.sigfl_backend.mail.domain.MailSender;
import org.example.sigfl_backend.mail.domain.model.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * No-op {@link MailSender} that logs the message instead of sending it. Active
 * when {@code mail.provider=log} (the default) — used for tests/CI and local dev
 * without an SMTP server.
 */
@Component
@ConditionalOnProperty(name = "mail.provider", havingValue = "log", matchIfMissing = true)
public class LogMailSender implements MailSender {

    private static final Logger log = LoggerFactory.getLogger(LogMailSender.class);

    @Override
    public void send(EmailMessage message) {
        log.info("[MAIL:log] to={} subject='{}' html={}\n{}",
                message.to(), message.subject(), message.html(), message.body());
    }
}
