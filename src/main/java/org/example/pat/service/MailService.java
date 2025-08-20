package org.example.pat.service;

import org.example.pat.entity.Curator;
import org.example.pat.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(User user, Curator curator) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Активация аккаунта");
        message.setText(buildEmailContent(user, curator));

        mailSender.send(message);
    }

    private String buildEmailContent(User user, Curator curator) {
        return String.format(
                "Уважаемый %s! Ваш аккаунт создан. Код активации: %s. Куратор: %s %s",
                user.getName(),
                user.getCode(),
                curator.getName(),
                curator.getSurname()
        );
    }
}