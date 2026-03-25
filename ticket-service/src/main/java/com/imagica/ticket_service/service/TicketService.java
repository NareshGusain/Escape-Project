package com.imagica.ticket_service.service;

import com.imagica.ticket_service.dto.TicketBookingRequest;
import com.imagica.ticket_service.dto.TicketBookingResponse;

public interface TicketService {
    TicketBookingResponse bookTickets(TicketBookingRequest request);
    TicketBookingResponse getBooking(Long bookingId);
    TicketBookingResponse cancelBooking(Long bookingId);
}
