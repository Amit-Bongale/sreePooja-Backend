package com.example.sreepooja.DTO.Request.Masters;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PincodeRequest {

    @NotEmpty(message = "Pincodes are required")
    private List<String> pincodes;

    private Boolean active = true;
}