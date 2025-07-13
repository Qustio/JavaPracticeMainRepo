package org.example.services;

import events.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-notification-topic", groupId = "notification-group")
    public void listenGroupFoo(UserEvent userEvent) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(userEvent.getEmail());
        m.setSubject(String.format("Аккаунт %s", userEvent.getEmail()));
        switch (userEvent.getOperation()){
            case Created -> m.setText("Здравствуйте! Ваш аккаунт на сайте был успешно создан");
            case Deleted -> m.setText("Здравствуйте! Ваш аккаунт был удалён");
        }
        mailSender.send(m);
    }
}
