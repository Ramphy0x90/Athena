package r16a.Athena.util;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import r16a.Athena.config.AppConfig;

/**
 * Utility service for sending emails using the Resend API.
 *
 * @author Ramphy Aquino Nova
 */
@Slf4j
@Service
public class EmailUtil {
    @Value("${athena.email.apikey}")
    private String emailApiKey;
    private Resend resend;
    private final AppConfig appConfig;

    public EmailUtil(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Initializes the Resend client with the provided API key.
     */
    @PostConstruct
    public void init() {
        resend = new Resend(emailApiKey);
        log.info("EmailUtil: email client created");
    }

    /**
     * Sends an email to the specified recipient.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param body    The HTML body of the email.
     */
    public void send(String to, String subject, String body) {
        CreateEmailOptions sendEmailRequest = CreateEmailOptions.builder()
                .from("No reply <athena@r16a.ch>")
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        if(appConfig.isProduction()) {
            try {
                resend.emails().send(sendEmailRequest);
                log.info("EmailUtil: email sent to {}", to);
            } catch (ResendException e) {
                log.error("EmailUtil: error sending email to {}", to);
            }
        } else {
            log.info("EmailUtil: email demo {}", body);
        }
    }
}
