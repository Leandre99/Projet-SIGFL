package org.example.sigfl_backend.mail.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mail configuration bound from {@code mail.*}. The SMTP transport itself is
 * configured through the standard {@code spring.mail.*} properties.
 *
 * <p>Switch provider with {@code mail.provider} (env {@code MAIL_PROVIDER}):
 * <ul>
 *   <li>{@code smtp} — send through an SMTP server (MailDev locally);</li>
 *   <li>{@code log}  — log the message instead of sending (default; tests/CI).</li>
 * </ul>
 */
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    /** Active provider: {@code smtp} or {@code log}. */
    private String provider = "log";

    /** Default "From" address. */
    private String from = "no-reply@sigfl.local";

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
