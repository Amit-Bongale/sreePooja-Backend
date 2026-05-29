package com.example.sreepooja.DTO.Request.Masters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityRequest {

    @NotBlank(message = "Community name is required")
    private String communityName;

    private Boolean active = true;
}