package com.example.sreepooja.DTO.Response.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriestResponse {

    private Long priestId;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private String whatsappNumber;

    private String city;

    private String state;

    private String languagesSpoken;

    private Long communityId;

    private String communityName;

    private PriestExperience experience;

    private Boolean active;
}
