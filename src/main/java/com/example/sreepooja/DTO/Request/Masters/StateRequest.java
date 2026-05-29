package com.example.sreepooja.DTO.Request.Masters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateRequest {

    @NotBlank(message = "State name is required")
    private String stateName;

    private Boolean active = true;
}