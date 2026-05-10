package com.imagica.show_service.mapper;

import com.imagica.show_service.dto.SeatInventoryResponseDTO;
import com.imagica.show_service.dto.ShowRequestDTO;
import com.imagica.show_service.dto.ShowResponseDTO;
import com.imagica.show_service.dto.ShowScheduleResponseDTO;
import com.imagica.show_service.entity.SeatInventory;
import com.imagica.show_service.entity.Show;
import com.imagica.show_service.entity.ShowSchedule;

import com.imagica.show_service.dto.ShowScheduleRequestDTO;
import com.imagica.show_service.dto.SeatInventoryRequestDTO;

public class ShowMapper {

    public static Show toEntity(ShowRequestDTO dto) {
        if (dto == null) return null;
        Show show = new Show();
        show.setName(dto.getName());
        show.setDescription(dto.getDescription());
        show.setDurationMinutes(dto.getDurationMinutes());
        show.setCategory(dto.getCategory());
        return show;
    }

    public static ShowSchedule toEntity(ShowScheduleRequestDTO dto, Show show) {
        if (dto == null) return null;
        ShowSchedule schedule = new ShowSchedule();
        schedule.setShow(show);
        schedule.setShowDate(dto.getShowDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setTotalSeats(dto.getTotalSeats());
        return schedule;
    }

    public static SeatInventory toEntity(SeatInventoryRequestDTO dto, ShowSchedule schedule) {
        if (dto == null) return null;
        SeatInventory inventory = new SeatInventory();
        inventory.setShowSchedule(schedule);
        inventory.setAvailableSeats(dto.getAvailableSeats());
        inventory.setReservedSeats(dto.getReservedSeats());
        return inventory;
    }

    public static ShowResponseDTO toShowResponseDTO(Show show) {
        if (show == null) return null;
        ShowResponseDTO dto = new ShowResponseDTO();
        dto.setId(show.getId());
        dto.setName(show.getName());
        dto.setDescription(show.getDescription());
        dto.setDurationMinutes(show.getDurationMinutes());
        dto.setCategory(show.getCategory());
        return dto;
    }

    public static ShowScheduleResponseDTO toShowScheduleResponseDTO(ShowSchedule schedule) {
        if (schedule == null) return null;
        ShowScheduleResponseDTO dto = new ShowScheduleResponseDTO();
        dto.setId(schedule.getId());
        if (schedule.getShow() != null) {
            dto.setShowId(schedule.getShow().getId());
        }
        dto.setShowDate(schedule.getShowDate());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setTotalSeats(schedule.getTotalSeats());
        return dto;
    }

    public static SeatInventoryResponseDTO toSeatInventoryResponseDTO(SeatInventory inventory) {
        if (inventory == null) return null;
        SeatInventoryResponseDTO dto = new SeatInventoryResponseDTO();
        dto.setId(inventory.getId());
        if (inventory.getShowSchedule() != null) {
            dto.setScheduleId(inventory.getShowSchedule().getId());
        }
        dto.setAvailableSeats(inventory.getAvailableSeats());
        dto.setReservedSeats(inventory.getReservedSeats());
        return dto;
    }
}
