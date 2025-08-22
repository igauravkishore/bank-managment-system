package com.bankingsystem.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);
    private final EmailService emailService;


    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void consume(String message) {
        LOGGER.info("Received notification event: {}", message);

        // Send email notification
        String to = "gvsinghania1997@gmail.com";  // Replace with real recipient
        String subject = "Transaction Notification";
        String body = "Dear User,\n\nA transaction event occurred:\n" + message + "\n\nThank you!";

        emailService.sendEmail(to, subject, body);

        System.out.println("Email sent successfully to " + to);
    }
}
