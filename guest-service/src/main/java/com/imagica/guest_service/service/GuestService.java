package com.imagica.guest_service.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imagica.guest_service.dto.GuestRequestDTO;
import com.imagica.guest_service.dto.GuestResponseDTO;
import com.imagica.guest_service.entity.Guest;
import com.imagica.guest_service.mapper.GuestMapper;
import com.imagica.guest_service.repository.GuestRepository;


/**
 * Service layer for guest business logic.
 */
@Service
public class GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestService.class);
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }


    /**
     * Creates a new guest.
     */
    @Transactional
    public GuestResponseDTO createGuest(GuestRequestDTO request) {
        logger.info("Creating guest with email: {}", request.getEmail());
        Guest guest = GuestMapper.toEntity(request);
        guest.setCreatedAt(LocalDateTime.now());
        guest.setUpdatedAt(LocalDateTime.now());
        Guest saved = guestRepository.save(guest);
        logger.info("Guest created with id: {}", saved.getId());
        return GuestMapper.toResponseDTO(saved);
    }


    /**
     * Retrieves a guest by id.
     */
    public Optional<GuestResponseDTO> getGuest(Long id) {
        logger.info("Fetching guest with id: {}", id);
        return guestRepository.findById(id).map(GuestMapper::toResponseDTO);
    }


    /**
     * Updates an existing guest.
     */
    @Transactional
    public Optional<GuestResponseDTO> updateGuest(Long id, GuestRequestDTO request) {
        logger.info("Updating guest with id: {}", id);
        Optional<Guest> guestOpt = guestRepository.findById(id);
        if (guestOpt.isPresent()) {
            Guest guest = guestOpt.get();
            guest.setFirstName(request.getFirstName());
            guest.setLastName(request.getLastName());
            guest.setEmail(request.getEmail());
            guest.setPhone(request.getPhone());
            guest.setUpdatedAt(LocalDateTime.now());
            guestRepository.save(guest);
            logger.info("Guest updated with id: {}", id);
            return Optional.of(GuestMapper.toResponseDTO(guest));
        }
        logger.warn("Guest not found for update, id: {}", id);
        return Optional.empty();
    }


    /**
     * Retrieves a guest by email.
     */
    public Optional<GuestResponseDTO> getGuestByEmail(String email) {
        logger.info("Fetching guest with email: {}", email);
        return guestRepository.findByEmail(email).map(GuestMapper::toResponseDTO);
    }
}
