package com.imagica.show_service.controller;

import com.imagica.show_service.dto.ApiResponse;
import com.imagica.show_service.dto.SeatInventoryResponseDTO;
import com.imagica.show_service.dto.ShowResponseDTO;
import com.imagica.show_service.dto.ShowScheduleResponseDTO;
import com.imagica.show_service.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
