package com.imagica.guest_service.mapper;

import java.time.format.DateTimeFormatter;

import com.imagica.guest_service.dto.GuestRequestDTO;
import com.imagica.guest_service.dto.GuestResponseDTO;
import com.imagica.guest_service.entity.Guest;

public class GuestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Guest toEntity(GuestRequestDTO dto) {
        Guest guest = new Guest();
        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setPhone(dto.getPhone());
        return guest;
    }

    public static GuestResponseDTO toResponseDTO(Guest guest) {
        GuestResponseDTO dto = new GuestResponseDTO();
        dto.setGuestId(guest.getId());
        dto.setFirstName(guest.getFirstName());
        dto.setLastName(guest.getLastName());
        dto.setEmail(guest.getEmail());
        dto.setPhone(guest.getPhone());
        dto.setUserRole(guest.getUserRole());
        dto.setCreatedAt(guest.getCreatedAt() != null ? guest.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(guest.getUpdatedAt() != null ? guest.getUpdatedAt().format(formatter) : null);
        return dto;
    }
}
