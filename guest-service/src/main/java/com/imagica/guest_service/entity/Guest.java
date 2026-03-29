package com.imagica.guest_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

//import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guest", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;


    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=(?:.*\\d){2,})(?=.*[^a-zA-Z0-9])\\S{8,64}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, at least two digits, one special character, and must not contain spaces"
    )
    private String password;

    private roleEnum userRole = roleEnum.CUSTOMER;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    // Getters and setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public String getFirstName() { return firstName; }
//    public void setFirstName(String firstName) { this.firstName = firstName; }
//    public String getLastName() { return lastName; }
//    public void setLastName(String lastName) { this.lastName = lastName; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getPhone() { return phone; }
//    public void setPhone(String phone) { this.phone = phone; }
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//    public LocalDateTime getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
