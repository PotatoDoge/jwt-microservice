package com.auth.jwtmicroservice.service;

import com.auth.jwtmicroservice.config.ConfigProperties;
import com.auth.jwtmicroservice.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class MailSenderService {

    private ConfigProperties configProperties;

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

            String url = "http://"+configProperties.getAddress() +":"+port+"/v1/auth/activateAccount/"+confirmationToken;

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
}
