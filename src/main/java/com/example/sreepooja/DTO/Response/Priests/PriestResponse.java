package com.example.sreepooja.DTO.Response.Priests;

import com.example.sreepooja.Enum.Priests.PriestExperience;
import lombok.Builder;
import lombok.Data;
import java.util.List;

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

    private List<String> languages;

    private Long communityId;

    private String communityName;

    private PriestExperience experience;

    private Boolean active;
}
