package com.imagica.show_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seat_inventory")
public class SeatInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false, unique = true)
    private ShowSchedule showSchedule;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "reserved_seats", nullable = false)
    private Integer reservedSeats;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ShowSchedule getShowSchedule() { return showSchedule; }
    public void setShowSchedule(ShowSchedule showSchedule) { this.showSchedule = showSchedule; }
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    public Integer getReservedSeats() { return reservedSeats; }
    public void setReservedSeats(Integer reservedSeats) { this.reservedSeats = reservedSeats; }
}
