package com.imagica.show_service.repository;

import com.imagica.show_service.entity.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
    Optional<SeatInventory> findByShowScheduleId(Long scheduleId);
}
