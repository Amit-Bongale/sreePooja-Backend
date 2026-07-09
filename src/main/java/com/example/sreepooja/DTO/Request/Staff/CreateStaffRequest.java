package com.example.sreepooja.DTO.Request.Staff;

import com.example.sreepooja.Enum.UserRoles;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStaffRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number"
    )
    private String mobileNo;

    @Email(message = "Invalid email")
    private String email;

    private LocalDate dob;

    @NotEmpty(message = "At least one role must be assigned")
    private Set<UserRoles> roles;

    @NotBlank
    private String password;
}