package com.imagica.guest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketBookedEvent {
    private Long bookingId;
    private Long guestId;
    private Long scheduleId;
    private List<String> seatNumbers;
    private Double totalAmount;
}