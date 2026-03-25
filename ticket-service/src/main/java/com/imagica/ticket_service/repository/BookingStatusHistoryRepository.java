package com.imagica.ticket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imagica.ticket_service.model.BookingStatusHistory;

public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, Long> {
}
