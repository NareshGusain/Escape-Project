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
import com.imagica.show_service.dto.ShowRequestDTO;
import com.imagica.show_service.dto.ShowScheduleRequestDTO;
import com.imagica.show_service.dto.SeatInventoryRequestDTO;

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

    public ShowResponseDTO createShow(ShowRequestDTO showRequestDTO) {
        logger.info("Creating new show: {}", showRequestDTO.getName());
        Show show = ShowMapper.toEntity(showRequestDTO);
        Show savedShow = showRepository.save(show);
        return ShowMapper.toShowResponseDTO(savedShow);
    }

    public ShowScheduleResponseDTO createShowSchedule(ShowScheduleRequestDTO requestDTO) {
        logger.info("Creating new schedule for show id: {}", requestDTO.getShowId());
        Show show = showRepository.findById(requestDTO.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + requestDTO.getShowId()));
        ShowSchedule schedule = ShowMapper.toEntity(requestDTO, show);
        ShowSchedule savedSchedule = showScheduleRepository.save(schedule);
        return ShowMapper.toShowScheduleResponseDTO(savedSchedule);
    }

    public SeatInventoryResponseDTO createSeatInventory(SeatInventoryRequestDTO requestDTO) {
        logger.info("Creating seat inventory for schedule id: {}", requestDTO.getScheduleId());
        ShowSchedule schedule = showScheduleRepository.findById(requestDTO.getScheduleId())
                .orElseThrow(() -> new RuntimeException("ShowSchedule not found with id: " + requestDTO.getScheduleId()));
        SeatInventory inventory = ShowMapper.toEntity(requestDTO, schedule);
        SeatInventory savedInventory = seatInventoryRepository.save(inventory);
        return ShowMapper.toSeatInventoryResponseDTO(savedInventory);
    }

    public ShowResponseDTO updateShow(Long id, ShowRequestDTO showRequestDTO) {
        logger.info("Updating show with id: {}", id);
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));
        show.setName(showRequestDTO.getName());
        show.setDescription(showRequestDTO.getDescription());
        show.setDurationMinutes(showRequestDTO.getDurationMinutes());
        show.setCategory(showRequestDTO.getCategory());
        Show updatedShow = showRepository.save(show);
        return ShowMapper.toShowResponseDTO(updatedShow);
    }

    public ShowScheduleResponseDTO updateShowSchedule(Long id, ShowScheduleRequestDTO requestDTO) {
        logger.info("Updating schedule with id: {}", id);
        ShowSchedule schedule = showScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShowSchedule not found with id: " + id));
        
        if (requestDTO.getShowId() != null && !requestDTO.getShowId().equals(schedule.getShow().getId())) {
            Show show = showRepository.findById(requestDTO.getShowId())
                    .orElseThrow(() -> new RuntimeException("Show not found with id: " + requestDTO.getShowId()));
            schedule.setShow(show);
        }
        
        schedule.setShowDate(requestDTO.getShowDate());
        schedule.setStartTime(requestDTO.getStartTime());
        schedule.setEndTime(requestDTO.getEndTime());
        schedule.setTotalSeats(requestDTO.getTotalSeats());
        ShowSchedule updatedSchedule = showScheduleRepository.save(schedule);
        return ShowMapper.toShowScheduleResponseDTO(updatedSchedule);
    }

    public SeatInventoryResponseDTO updateSeatInventory(Long id, SeatInventoryRequestDTO requestDTO) {
        logger.info("Updating seat inventory with id: {}", id);
        SeatInventory inventory = seatInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SeatInventory not found with id: " + id));
        
        if (requestDTO.getScheduleId() != null && !requestDTO.getScheduleId().equals(inventory.getShowSchedule().getId())) {
            ShowSchedule schedule = showScheduleRepository.findById(requestDTO.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("ShowSchedule not found with id: " + requestDTO.getScheduleId()));
            inventory.setShowSchedule(schedule);
        }
        
        inventory.setAvailableSeats(requestDTO.getAvailableSeats());
        inventory.setReservedSeats(requestDTO.getReservedSeats());
        SeatInventory updatedInventory = seatInventoryRepository.save(inventory);
        return ShowMapper.toSeatInventoryResponseDTO(updatedInventory);
    }

    @org.springframework.transaction.annotation.Transactional
    public void bookSeats(Long scheduleId, int ticketCount) {
        logger.info("Booking {} seats for schedule id: {}", ticketCount, scheduleId);
        SeatInventory inventory = seatInventoryRepository.findByShowScheduleId(scheduleId)
                .orElseThrow(() -> new RuntimeException("SeatInventory not found for schedule id: " + scheduleId));
        
        if (inventory.getAvailableSeats() < ticketCount) {
            throw new RuntimeException("Not enough available seats");
        }
        
        inventory.setAvailableSeats(inventory.getAvailableSeats() - ticketCount);
        inventory.setReservedSeats(inventory.getReservedSeats() + ticketCount);
        seatInventoryRepository.save(inventory);
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
