package com.auth.jwtmicroservice.service;

import com.auth.jwtmicroservice.config.ConfigProperties.ServerConfigProperties;
import com.auth.jwtmicroservice.config.ConfigProperties.FrontendConfigProperties;
import com.auth.jwtmicroservice.entity.ResetPasswordToken;
import com.auth.jwtmicroservice.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class MailSenderService {

    private FrontendConfigProperties frontendConfigProperties;

    private JavaMailSender emailSender;

    private final String port = "8080";

    @Async
    public void sendSimpleMessage(User registeredUser, String confirmationToken) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        try {
            String url = frontendConfigProperties.getTokenValidationScreen() + "?confirmation-token=" + confirmationToken;
            helper.setTo(registeredUser.getEmail());
            helper.setSubject("Verify your account!");
            helper.setText(buildEmailTemplate(registeredUser.getFullName(), url), true);
            emailSender.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    private String buildEmailTemplate(String name, String confirmationLink) {
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Template</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello, %s!</p>\n" +
                "    <p>We hope you're doing well!</p>\n" +
                "    <p>Welcome! We hope that our product helps you in your day to day tasks</p>\n" +
                "    <p><i>To activate your account please click on the following link:</i></p>\n" +
                "    <p><a href=\"%s\">Activate Your Account</a></p>\n" +
                "    <p>Please do not reply to this message. It is an automatic email.\n" +
                "    <p>If there is an issue please contact customer support.\n" +
                "</body>\n" +
                "</html>";

        return String.format(template, name, confirmationLink);
    }

    public void sendResetPasswordMail(ResetPasswordToken resetPasswordToken) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        try {
            String url = frontendConfigProperties.getPasswordResetScreen() + "?reset-psw-token=" + resetPasswordToken.getToken();
            helper.setTo(resetPasswordToken.getUser().getEmail());
            helper.setSubject("Reset password");
            helper.setText(buildResetPasswordMail(url), true);
            emailSender.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    private String buildResetPasswordMail(String url) {
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Template</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello\n" +
                "    <p>We hope you're doing well!</p>\n" +
                "    <p><i>To reset your password please click on the following link:</i></p>\n" +
                "    <p><a href=\"%s\">Reset password</a></p>\n" +
                "    <p>Please do not reply to this message. It is an automatic email.\n" +
                "    <p>If there is an issue please contact customer support.\n" +
                "</body>\n" +
                "</html>";

        return String.format(template, url);
    }
}
