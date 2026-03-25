package com.imagica.ticket_service.model;

import jakarta.persistence.*;

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

    // Getters and setters
    // ...

    public enum TicketStatus {
        CREATED,
        CONFIRMED,
        CANCELLED
    }
}
