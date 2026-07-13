package com.example.sreepooja.DTO.Request.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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

    private String password;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotNull
    private Long stateId;

    @NotNull
    private Long cityId;

    @NotNull
    private Long pincodeId;

    @NotEmpty
    private List<Long> languageIds;

    @NotNull
    private Long communityId;

    @NotNull
    private PriestExperience experience;

    private String referredBy;

    private Boolean active;

    private String bankingName;

    private String bankName;

    private String bankBranchName;

    private String bankIfscCode;

    private String bankAccountNumber;

    private String upiId;
}
