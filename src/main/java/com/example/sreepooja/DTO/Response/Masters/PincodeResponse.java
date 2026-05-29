package com.example.sreepooja.DTO.Response.Masters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PincodeResponse {

    private Long id;

    private String pincode;

    private Boolean active;
}
