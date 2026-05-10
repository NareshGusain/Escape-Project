package com.imagica.guest_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagica.guest_service.dto.GuestRequestDTO;
import com.imagica.guest_service.dto.GuestResponseDTO;
import com.imagica.guest_service.entity.roleEnum;
import com.imagica.guest_service.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuestController.class)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Autowired
    private ObjectMapper objectMapper;

    private GuestRequestDTO validRequest;
    private GuestResponseDTO validResponse;

    @BeforeEach
    void setUp() {
        validRequest = new GuestRequestDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                "PassWord123!"
        );

        validResponse = new GuestResponseDTO(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                roleEnum.CUSTOMER,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );
    }

    @Test
    void createGuest_ShouldReturnCreatedGuest() throws Exception {
        Mockito.when(guestService.createGuest(any(GuestRequestDTO.class))).thenReturn(validResponse);

        mockMvc.perform(post("/api/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Guest created successfully"))
                .andExpect(jsonPath("$.data.guestId").value(1))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    void createGuest_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        GuestRequestDTO invalidRequest = new GuestRequestDTO(
                "", // Blank first name
                "Doe",
                "invalid-email", // Invalid email format
                "123", // Invalid phone pattern
                "pass" // Invalid password pattern/size
        );

        mockMvc.perform(post("/api/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getGuest_WhenGuestExists_ShouldReturnGuest() throws Exception {
        Mockito.when(guestService.getGuest(1L)).thenReturn(Optional.of(validResponse));

        mockMvc.perform(get("/api/guests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    void getGuest_WhenGuestDoesNotExist_ShouldReturnNotFound() throws Exception {
        Mockito.when(guestService.getGuest(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/guests/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Guest not found"));
    }

    @Test
    void updateGuest_WhenGuestExists_ShouldReturnUpdatedGuest() throws Exception {
        Mockito.when(guestService.updateGuest(eq(1L), any(GuestRequestDTO.class))).thenReturn(Optional.of(validResponse));

        mockMvc.perform(put("/api/guests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    void updateGuest_WhenGuestDoesNotExist_ShouldReturnNotFound() throws Exception {
        Mockito.when(guestService.updateGuest(eq(99L), any(GuestRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/guests/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getGuestByEmail_WhenGuestExists_ShouldReturnGuest() throws Exception {
        Mockito.when(guestService.getGuestByEmail("john.doe@example.com")).thenReturn(Optional.of(validResponse));

        mockMvc.perform(get("/api/guests/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.guestId").value(1));
    }

    @Test
    void getGuestByEmail_WhenGuestDoesNotExist_ShouldReturnNotFound() throws Exception {
        Mockito.when(guestService.getGuestByEmail("unknown@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/guests/email/unknown@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
