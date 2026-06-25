package com.example.sreepooja.DTO.Response.Staff;

import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class StaffResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String email;

    private LocalDate dob;

    private UserStatus status;

    private Set<UserRoles> roles;
}