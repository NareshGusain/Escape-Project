package com.imagica.ticket_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "show-service", url = "http://localhost:8082") // Assuming show-service is on 8082
public interface ShowServiceClient {

    @PostMapping("/api/schedules/{id}/book-seats")
    void bookSeats(@PathVariable("id") Long id, @RequestParam("ticketCount") int ticketCount);
}
