package com.imagica.ticket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imagica.ticket_service.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
