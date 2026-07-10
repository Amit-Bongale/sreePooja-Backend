package com.example.sreepooja.DTO.Response.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import com.example.sreepooja.Enum.Priests.PriestRegistrationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PriestRegistrationDetailsResponse {

    private Long registrationId;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String mobileNumber;

    private String whatsappNumber;

    private String email;

    private String gothra;

    private String pravara;

    private String nativePlace;

    private String aadhaarNumber;

    private String addressLine1;

    private String addressLine2;

    private String state;

    private String city;

    private String pincode;

    private String community;

    private List<String> languages;

    private PriestExperience experience;

    private String referredBy;

    private String bankingName;

    private String bankName;

    private String bankBranchName;

    private String bankIfscCode;

    private String bankAccountNumber;

    private String upiId;

    private String priestPhotoUrl;

    private String aadhaarPdfUrl;

    private PriestRegistrationStatus status;

    private LocalDateTime createdAt;
}