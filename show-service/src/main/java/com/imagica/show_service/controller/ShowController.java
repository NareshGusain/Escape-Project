package com.imagica.show_service.controller;

import com.imagica.show_service.dto.ApiResponse;
import com.imagica.show_service.dto.SeatInventoryResponseDTO;
import com.imagica.show_service.dto.ShowResponseDTO;
import com.imagica.show_service.dto.ShowRequestDTO;
import com.imagica.show_service.dto.ShowScheduleRequestDTO;
import com.imagica.show_service.dto.SeatInventoryRequestDTO;
import com.imagica.show_service.dto.ShowScheduleResponseDTO;
import com.imagica.show_service.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShowController {
    private static final Logger logger = LoggerFactory.getLogger(ShowController.class);
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/shows")
    public ResponseEntity<ApiResponse<List<ShowResponseDTO>>> getAllShows() {
        logger.info("API: Fetch all shows");
        List<ShowResponseDTO> shows = showService.getAllShows();
        ApiResponse<List<ShowResponseDTO>> response = new ApiResponse<>(true, "Shows fetched successfully", shows);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/shows")
    public ResponseEntity<ApiResponse<ShowResponseDTO>> createShow(@RequestBody ShowRequestDTO showRequestDTO) {
        logger.info("API: Create a new show");
        ShowResponseDTO createdShow = showService.createShow(showRequestDTO);
        ApiResponse<ShowResponseDTO> response = new ApiResponse<>(true, "Show created successfully", createdShow);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/shows/{id}")
    public ResponseEntity<ApiResponse<ShowResponseDTO>> getShowById(@PathVariable Long id) {
        logger.info("API: Fetch show details for id: {}", id);
        Optional<ShowResponseDTO> showOpt = showService.getShowById(id);
        
        if (showOpt.isPresent()) {
            ApiResponse<ShowResponseDTO> response = new ApiResponse<>(true, "Show details fetched successfully", showOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<ShowResponseDTO> response = new ApiResponse<>(false, "Show not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/shows/{id}/schedules")
    public ResponseEntity<ApiResponse<List<ShowScheduleResponseDTO>>> getSchedulesByShowId(@PathVariable Long id) {
        logger.info("API: Fetch schedules for show id: {}", id);
        List<ShowScheduleResponseDTO> schedules = showService.getSchedulesByShowId(id);
        ApiResponse<List<ShowScheduleResponseDTO>> response = new ApiResponse<>(true, "Schedules fetched successfully", schedules);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedules/{id}/availability")
    public ResponseEntity<ApiResponse<SeatInventoryResponseDTO>> getSeatAvailabilityByScheduleId(@PathVariable Long id) {
        logger.info("API: Fetch seat availability for schedule id: {}", id);
        Optional<SeatInventoryResponseDTO> inventoryOpt = showService.getSeatAvailabilityByScheduleId(id);
        
        if (inventoryOpt.isPresent()) {
            ApiResponse<SeatInventoryResponseDTO> response = new ApiResponse<>(true, "Seat availability fetched successfully", inventoryOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<SeatInventoryResponseDTO> response = new ApiResponse<>(false, "Seat inventory not found for schedule", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/schedules")
    public ResponseEntity<ApiResponse<ShowScheduleResponseDTO>> createShowSchedule(@RequestBody ShowScheduleRequestDTO requestDTO) {
        logger.info("API: Create a new show schedule");
        ShowScheduleResponseDTO createdSchedule = showService.createShowSchedule(requestDTO);
        ApiResponse<ShowScheduleResponseDTO> response = new ApiResponse<>(true, "Show schedule created successfully", createdSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/seat-inventory")
    public ResponseEntity<ApiResponse<SeatInventoryResponseDTO>> createSeatInventory(@RequestBody SeatInventoryRequestDTO requestDTO) {
        logger.info("API: Create seat inventory for schedule");
        SeatInventoryResponseDTO createdInventory = showService.createSeatInventory(requestDTO);
        ApiResponse<SeatInventoryResponseDTO> response = new ApiResponse<>(true, "Seat inventory created successfully", createdInventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/shows/{id}")
    public ResponseEntity<ApiResponse<ShowResponseDTO>> updateShow(@PathVariable Long id, @RequestBody ShowRequestDTO showRequestDTO) {
        logger.info("API: Update show details for id: {}", id);
        ShowResponseDTO updatedShow = showService.updateShow(id, showRequestDTO);
        ApiResponse<ShowResponseDTO> response = new ApiResponse<>(true, "Show updated successfully", updatedShow);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<ApiResponse<ShowScheduleResponseDTO>> updateShowSchedule(@PathVariable Long id, @RequestBody ShowScheduleRequestDTO requestDTO) {
        logger.info("API: Update schedule details for id: {}", id);
        ShowScheduleResponseDTO updatedSchedule = showService.updateShowSchedule(id, requestDTO);
        ApiResponse<ShowScheduleResponseDTO> response = new ApiResponse<>(true, "Show schedule updated successfully", updatedSchedule);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/seat-inventory/{id}")
    public ResponseEntity<ApiResponse<SeatInventoryResponseDTO>> updateSeatInventory(@PathVariable Long id, @RequestBody SeatInventoryRequestDTO requestDTO) {
        logger.info("API: Update seat inventory for id: {}", id);
        SeatInventoryResponseDTO updatedInventory = showService.updateSeatInventory(id, requestDTO);
        ApiResponse<SeatInventoryResponseDTO> response = new ApiResponse<>(true, "Seat inventory updated successfully", updatedInventory);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/schedules/{id}/book-seats")
    public ResponseEntity<ApiResponse<Void>> bookSeats(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestParam int ticketCount) {
        logger.info("API: Book seats for schedule id: {}", id);
        showService.bookSeats(id, ticketCount);
        ApiResponse<Void> response = new ApiResponse<>(true, "Seats booked successfully", null);
        return ResponseEntity.ok(response);
    }
}
