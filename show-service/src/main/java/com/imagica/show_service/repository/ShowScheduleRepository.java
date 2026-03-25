package com.imagica.show_service.repository;

import com.imagica.show_service.entity.ShowSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowScheduleRepository extends JpaRepository<ShowSchedule, Long> {
    List<ShowSchedule> findByShowId(Long showId);
}
