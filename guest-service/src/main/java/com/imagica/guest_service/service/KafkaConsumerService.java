package com.imagica.guest_service.service;

import com.imagica.guest_service.dto.TicketBookedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "ticket-events", groupId = "guest-service-group")
    public void consumeTicketEvent(TicketBookedEvent event) {
        System.out.println("=========================================");
        System.out.println("GUEST SERVICE RECEIVED KAFKA EVENT: " + event);
        System.out.println("Total Spent: INR " + event.getTotalAmount());
        System.out.println("=========================================");
    }
}
