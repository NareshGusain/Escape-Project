package com.imagica.show_service.service;

import com.imagica.show_service.dto.SeatInventoryResponseDTO;
import com.imagica.show_service.dto.ShowResponseDTO;
import com.imagica.show_service.dto.ShowScheduleResponseDTO;
import com.imagica.show_service.entity.SeatInventory;
import com.imagica.show_service.entity.Show;
import com.imagica.show_service.entity.ShowSchedule;
import com.imagica.show_service.mapper.ShowMapper;
import com.imagica.show_service.repository.SeatInventoryRepository;
import com.imagica.show_service.repository.ShowRepository;
import com.imagica.show_service.repository.ShowScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowService {
    private static final Logger logger = LoggerFactory.getLogger(ShowService.class);

    private final ShowRepository showRepository;
    private final ShowScheduleRepository showScheduleRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    public ShowService(ShowRepository showRepository,
                       ShowScheduleRepository showScheduleRepository,
                       SeatInventoryRepository seatInventoryRepository) {
        this.showRepository = showRepository;
        this.showScheduleRepository = showScheduleRepository;
        this.seatInventoryRepository = seatInventoryRepository;
    }

    public List<ShowResponseDTO> getAllShows() {
        logger.info("Fetching all shows");
        return showRepository.findAll().stream()
                .map(ShowMapper::toShowResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ShowResponseDTO> getShowById(Long id) {
        logger.info("Fetching show details for id: {}", id);
        return showRepository.findById(id).map(ShowMapper::toShowResponseDTO);
    }

    public List<ShowScheduleResponseDTO> getSchedulesByShowId(Long showId) {
        logger.info("Fetching schedules for show id: {}", showId);
        return showScheduleRepository.findByShowId(showId).stream()
                .map(ShowMapper::toShowScheduleResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<SeatInventoryResponseDTO> getSeatAvailabilityByScheduleId(Long scheduleId) {
        logger.info("Fetching seat availability for schedule id: {}", scheduleId);
        return seatInventoryRepository.findByShowScheduleId(scheduleId)
                .map(ShowMapper::toSeatInventoryResponseDTO);
    }
}
