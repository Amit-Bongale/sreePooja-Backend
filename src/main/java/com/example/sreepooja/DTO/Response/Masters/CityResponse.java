package com.example.sreepooja.DTO.Response.Masters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CityResponse {

    private Long id;

    private String cityName;

    private String stateName;

    private Boolean active;

    private List<PincodeResponse> pincodes;
}