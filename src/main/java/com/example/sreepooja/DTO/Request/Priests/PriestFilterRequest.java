package com.example.sreepooja.DTO.Request.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import com.example.sreepooja.Enum.Priests.Trimathastharu;
import lombok.Data;

@Data
public class PriestFilterRequest {

    private Boolean active;

    private String mobileNumber;

    private Trimathastharu trimathastharu;

    private PriestExperience experience;

    private Long languageId;

    private Long cityId;
}
