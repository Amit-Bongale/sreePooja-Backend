package com.example.sreepooja.DTO.Response.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PriestDetailsResponse {

    private Long priestId;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String gothra;

    private String pravara;

    private String nativePlace;

    private String aadhaarNumber;

    private String mobileNumber;

    private String whatsappNumber;

    private String email;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    private List<String> languages;

    private Long communityId;

    private String communityName;

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

    private Boolean active;

    private LocalDateTime createdAt;
}
