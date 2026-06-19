package com.example.sreepooja.DTO.Response.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String languagesSpoken;

    private Long communityId;

    private String communityName;

    private PriestExperience experience;

    private String referredBy;

    private Boolean active;

    private LocalDateTime createdAt;
}
