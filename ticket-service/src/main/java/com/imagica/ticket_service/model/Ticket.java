package com.imagica.ticket_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private TicketBooking booking;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    public enum TicketStatus {
        CREATED,
        CONFIRMED,
        CANCELLED
    }
}
