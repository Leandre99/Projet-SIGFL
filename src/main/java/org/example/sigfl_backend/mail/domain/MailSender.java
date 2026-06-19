package org.example.sigfl_backend.mail.domain;

import org.example.sigfl_backend.mail.domain.model.EmailMessage;

/**
 * Port for sending email. Implementations are interchangeable adapters (SMTP,
 * log, ...) selected at runtime via the {@code mail.provider} property.
 */
public interface MailSender {

    void send(EmailMessage message);
}
