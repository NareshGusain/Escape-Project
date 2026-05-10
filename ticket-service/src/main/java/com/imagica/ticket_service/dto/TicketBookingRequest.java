package com.imagica.ticket_service.dto;

import lombok.Data;

@Data
public class TicketBookingRequest {
    private Long guestId;
    private Long scheduleId;
    private Integer ticketCount;
}
