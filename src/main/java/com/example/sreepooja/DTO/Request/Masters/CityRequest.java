package com.example.sreepooja.DTO.Request.Masters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequest {

    @NotBlank(message = "City name is required")
    private String cityName;

    private Boolean active = true;
}