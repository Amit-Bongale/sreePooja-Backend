package com.example.sreepooja.DTO.Request.Staff;

import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffRequest {

    private String firstName;

    private String lastName;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number"
    )
    private String mobileNo;

    @Email(message = "Invalid email")
    private String email;

    private LocalDate dob;

    private UserStatus status;

    private Set<UserRoles> roles;

    private String password;
}
