package com.imagica.ticket_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ticket_booking")
public class TicketBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long guestId;
    private Long scheduleId;
    private Integer ticketCount;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    // Getters and setters
    // ...

    public enum BookingStatus {
        CREATED,
        PAYMENT_PENDING,
        CONFIRMED,
        CANCELLED
    }
}