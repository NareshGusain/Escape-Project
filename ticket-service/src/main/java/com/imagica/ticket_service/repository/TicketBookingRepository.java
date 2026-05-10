package com.imagica.ticket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imagica.ticket_service.model.TicketBooking;

public interface TicketBookingRepository extends JpaRepository<TicketBooking, Long> {
}
