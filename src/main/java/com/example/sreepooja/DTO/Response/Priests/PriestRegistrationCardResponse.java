package com.example.sreepooja.DTO.Response.Priests;

import com.example.sreepooja.Enum.Priests.PriestRegistrationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PriestRegistrationCardResponse {

    private Long registrationId;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private PriestRegistrationStatus status;

    private LocalDateTime createdAt;
}