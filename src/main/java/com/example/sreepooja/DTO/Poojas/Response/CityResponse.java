package com.example.sreepooja.DTO.Poojas.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityResponse {

    private Long id;

    private String cityName;
}