package com.example.sreepooja.DTO.Request.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import lombok.Data;

@Data
public class PriestFilterRequest {

    private Boolean active;

    private String name;

    private String mobileNumber;

    private Long communityId;

    private PriestExperience experience;

    private Long languageId;

    private Long cityId;
}
