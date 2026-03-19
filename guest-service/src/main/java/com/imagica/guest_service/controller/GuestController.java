package com.imagica.guest_service.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imagica.guest_service.dto.ApiResponse;
import com.imagica.guest_service.dto.GuestRequestDTO;
import com.imagica.guest_service.dto.GuestResponseDTO;
import com.imagica.guest_service.service.GuestService;


/**
 * REST controller for guest APIs.
 */
@RestController
@RequestMapping("api/guests")
public class GuestController {
        private static final Logger logger = LoggerFactory.getLogger(GuestController.class);
        private GuestService guestService;
        

        public GuestController(GuestService guestService) {
                this.guestService = guestService;
        }

    /**
     * Create a new guest.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GuestResponseDTO>> createGuest(
            @javax.validation.Valid @RequestBody GuestRequestDTO request) {
        logger.info("API: Create guest called for email: {}", request.getEmail());
        GuestResponseDTO response = guestService.createGuest(request);
        ApiResponse<GuestResponseDTO> apiResponse =
                new ApiResponse<>(true, "Guest created successfully", response);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * Get guest by id.
     */
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<GuestResponseDTO>> getGuest(@PathVariable Long id) {
                logger.info("API: Get guest called for id: {}", id);
                Optional<GuestResponseDTO> guestOpt = guestService.getGuest(id);
                ApiResponse<GuestResponseDTO> apiResponse;
                if (guestOpt.isPresent()) {
                        apiResponse = new ApiResponse<>(true, "Guest fetched successfully", guestOpt.get());
                        return ResponseEntity.ok(apiResponse);
                } else {
                        apiResponse = new ApiResponse<>(false, "Guest not found", null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
                }
        }

    /**
     * Update guest by id.
     */
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<GuestResponseDTO>> updateGuest(@PathVariable Long id, @RequestBody GuestRequestDTO request) {
                logger.info("API: Update guest called for id: {}", id);
                Optional<GuestResponseDTO> guestOpt = guestService.updateGuest(id, request);
                ApiResponse<GuestResponseDTO> apiResponse;
                if (guestOpt.isPresent()) {
                        apiResponse = new ApiResponse<>(true, "Guest updated successfully", guestOpt.get());
                        return ResponseEntity.ok(apiResponse);
                } else {
                        apiResponse = new ApiResponse<>(false, "Guest not found", null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
                }
        }

    /**
     * Get guest by email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<GuestResponseDTO>> getGuestByEmail(@PathVariable String email) {
        logger.info("API: Get guest by email: {}", email);
        return guestService.getGuestByEmail(email)
                .map(guest -> ResponseEntity.ok(new ApiResponse<>(true, "Guest fetched successfully", guest)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Guest not found", null)));
    }
}
