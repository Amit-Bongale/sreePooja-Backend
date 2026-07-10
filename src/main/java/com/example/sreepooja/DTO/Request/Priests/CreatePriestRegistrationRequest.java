package com.example.sreepooja.DTO.Request.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreatePriestRegistrationRequest {

    @NotBlank
    private String firstName;

    private String lastName;

    @NotNull
    private LocalDate dob;

    @NotBlank
    private String mobileNumber;

    private String whatsappNumber;

    @Email
    private String email;

    @NotBlank
    private String gothra;

    private String pravara;

    @NotBlank
    private String nativePlace;

    @NotBlank
    private String aadhaarNumber;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotNull
    private Long stateId;

    @NotNull
    private Long cityId;

    @NotNull
    private Long pincodeId;

    @NotNull
    private Long communityId;

    @NotEmpty
    private List<Long> languageIds;

    @NotNull
    private PriestExperience experience;

    private String referredBy;

    @NotBlank
    private String bankingName;

    @NotBlank
    private String bankName;

    @NotBlank
    private String bankBranchName;

    @NotBlank
    private String bankIfscCode;

    @NotBlank
    private String bankAccountNumber;

    private String upiId;
}