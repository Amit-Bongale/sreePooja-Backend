package com.example.sreepooja.DTO.Request.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePriestRequest {

    @NotBlank
    private String firstName;

    private String lastName;

    @NotNull
    private LocalDate dob;

    @NotBlank
    private String gothra;

    private String pravara;

    @NotBlank
    private String nativePlace;

    @NotBlank
    private String aadhaarNumber;

    @NotBlank
    private String mobileNumber;

    private String whatsappNumber;

    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;

    private String languagesSpoken;

    @NotNull
    private Long communityId;

    @NotNull
    private PriestExperience experience;

    private String referredBy;

    private Boolean active;
}
