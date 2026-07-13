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

    private String community;

    private String state;

    private String city;

    private String pincode;

    private String priestPhotoUrl;

    private PriestRegistrationStatus status;

    private LocalDateTime createdAt;
}