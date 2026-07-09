package com.example.sreepooja.DTO.Request.Staff;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffProfileRequest {

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email")
    private String email;

    private LocalDate dob;

    private String password;
}