package com.imagica.ticket_service.dto;

import java.util.List;
import lombok.Data;

@Data
public class TicketBookingResponse {
    private Long bookingId;
    private List<String> seatNumbers;
    private String bookingStatus;
    private Double totalPrice;
}
