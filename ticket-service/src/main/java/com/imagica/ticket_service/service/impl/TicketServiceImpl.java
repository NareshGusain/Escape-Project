package com.imagica.ticket_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imagica.ticket_service.client.ShowServiceClient;
import com.imagica.ticket_service.dto.TicketBookedEvent;
import com.imagica.ticket_service.dto.TicketBookingRequest;
import com.imagica.ticket_service.dto.TicketBookingResponse;
import com.imagica.ticket_service.model.BookingStatusHistory;
import com.imagica.ticket_service.model.Ticket;
import com.imagica.ticket_service.model.TicketBooking;
import com.imagica.ticket_service.repository.BookingStatusHistoryRepository;
import com.imagica.ticket_service.repository.TicketBookingRepository;
import com.imagica.ticket_service.repository.TicketRepository;
import com.imagica.ticket_service.service.TicketService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketBookingRepository ticketBookingRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BookingStatusHistoryRepository bookingStatusHistoryRepository;
    @Autowired
    private ShowServiceClient showServiceClient;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    @Transactional
    public TicketBookingResponse bookTickets(TicketBookingRequest request) {
        // Call show-service to update seat inventory
        showServiceClient.bookSeats(request.getScheduleId(), request.getTicketCount());

        TicketBooking booking = new TicketBooking();
        booking.setGuestId(request.getGuestId());
        booking.setScheduleId(request.getScheduleId());
        booking.setTicketCount(request.getTicketCount());
        booking.setTotalPrice(request.getTicketCount() * 50.0); // Assuming 50.0 per ticket
        booking.setBookingStatus(TicketBooking.BookingStatus.CREATED);
        booking.setCreatedAt(LocalDateTime.now());

        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i <= request.getTicketCount(); i++) {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setSeatNumber("S" + i + "-" + System.currentTimeMillis() % 1000); 
            ticket.setTicketStatus(Ticket.TicketStatus.CREATED);
            tickets.add(ticket);
        }
        booking.setTickets(tickets);

        TicketBooking savedBooking = ticketBookingRepository.save(booking);

        BookingStatusHistory history = new BookingStatusHistory();
        history.setBooking(savedBooking);
        history.setStatus(TicketBooking.BookingStatus.CREATED);
        history.setUpdatedAt(LocalDateTime.now());
        bookingStatusHistoryRepository.save(history);

        // --- KAFKA PRODUCER LOGIC ---
        // 1. Get the seat numbers
        List<String> bookedSeats = tickets.stream()
                .map(Ticket::getSeatNumber)
                .collect(Collectors.toList());

        // 2. Create the event object
        TicketBookedEvent event = new TicketBookedEvent(
                savedBooking.getId(),
                savedBooking.getGuestId(),
                savedBooking.getScheduleId(),
                bookedSeats,
                savedBooking.getTotalPrice()
        );

        // 3. Send it to the "ticket-events" topic
        kafkaTemplate.send("ticket-events", event);
        // ----------------------------- 
        return mapToResponse(savedBooking);
    }

    @Override
    public TicketBookingResponse getBooking(Long bookingId) {
        TicketBooking booking = ticketBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        return mapToResponse(booking);
    }

    @Override
    @Transactional
    public TicketBookingResponse cancelBooking(Long bookingId) {
        TicketBooking booking = ticketBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        
        booking.setBookingStatus(TicketBooking.BookingStatus.CANCELLED);
        for (Ticket ticket : booking.getTickets()) {
            ticket.setTicketStatus(Ticket.TicketStatus.CANCELLED);
        }
        TicketBooking updatedBooking = ticketBookingRepository.save(booking);

        BookingStatusHistory history = new BookingStatusHistory();
        history.setBooking(updatedBooking);
        history.setStatus(TicketBooking.BookingStatus.CANCELLED);
        history.setUpdatedAt(LocalDateTime.now());
        bookingStatusHistoryRepository.save(history);

        return mapToResponse(updatedBooking);
    }

    private TicketBookingResponse mapToResponse(TicketBooking booking) {
        TicketBookingResponse response = new TicketBookingResponse();
        response.setBookingId(booking.getId());
        response.setBookingStatus(booking.getBookingStatus().name());
        response.setTotalPrice(booking.getTotalPrice());
        if (booking.getTickets() != null) {
            List<String> seatNumbers = booking.getTickets().stream()
                    .map(Ticket::getSeatNumber)
                    .collect(Collectors.toList());
            response.setSeatNumbers(seatNumbers);
        }
        return response;
    }
}
