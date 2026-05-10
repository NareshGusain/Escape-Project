package com.imagica.ticket_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imagica.ticket_service.dto.TicketBookingRequest;
import com.imagica.ticket_service.dto.TicketBookingResponse;
import com.imagica.ticket_service.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/book")
    public TicketBookingResponse bookTickets(@RequestBody TicketBookingRequest request) {
        return ticketService.bookTickets(request);
    }

    @GetMapping("/{bookingId}")
    public TicketBookingResponse getBooking(@PathVariable Long bookingId) {
        return ticketService.getBooking(bookingId);
    }

    @PutMapping("/{bookingId}/cancel")
    public TicketBookingResponse cancelBooking(@PathVariable Long bookingId) {
        return ticketService.cancelBooking(bookingId);
    }
}
