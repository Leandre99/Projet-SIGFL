package org.example.sigfl_backend.mail.domain.model;

import java.util.List;

/**
 * A provider-agnostic email to send.
 *
 * @param to      recipient addresses (at least one)
 * @param subject subject line
 * @param body    message body
 * @param html    whether {@code body} is HTML ({@code true}) or plain text
 */
public record EmailMessage(List<String> to, String subject, String body, boolean html) {

    public static EmailMessage text(String to, String subject, String body) {
        return new EmailMessage(List.of(to), subject, body, false);
    }

    public static EmailMessage html(String to, String subject, String body) {
        return new EmailMessage(List.of(to), subject, body, true);
    }
}
