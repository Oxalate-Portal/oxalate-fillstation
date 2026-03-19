package io.oxalate.fillstation.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromAddress;

    public void sendEmail(String to, String subject, String templateName, String language, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }

            String templatePath = "email/" + language + "/" + templateName;
            String htmlContent = templateEngine.process(templatePath, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Email sent to {} with subject '{}'", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    public void sendVerificationEmail(String to, String name, String verificationLink, String language) {
        sendEmail(to, "Verify your email address",
                "verification-email", language,
                Map.of("name", name, "verificationLink", verificationLink));
    }

    public void sendPasswordResetEmail(String to, String name, String resetLink, String language) {
        sendEmail(to, "Reset your password",
                "password-reset-email", language,
                Map.of("name", name, "resetLink", resetLink));
    }

    public void sendAccountApprovedEmail(String to, String name, String language) {
        sendEmail(to, "Your account has been approved",
                "account-approved-email", language,
                Map.of("name", name));
    }

    public void sendFillsZeroedEmail(String to, String name, String language) {
        sendEmail(to, "Your fills have been zeroed",
                "fills-zeroed-email", language,
                Map.of("name", name));
    }

    public void sendGasUsageNotificationEmail(String to, String name, Object summary, String language) {
        sendEmail(to, "Gas usage notification",
                "gas-usage-notification-email", language,
                Map.of("name", name, "summary", summary));
    }
}
